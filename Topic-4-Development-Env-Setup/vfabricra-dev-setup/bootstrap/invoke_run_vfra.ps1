Param(
  [parameter(Mandatory=$true)]
  [string]$viServer,
  [parameter(Mandatory=$true)]
  [string]$viUser,
  [parameter(Mandatory=$true)]
  [string]$viPassword,
  [parameter(Mandatory=$true)]
  [string]$vmName,
  [parameter(Mandatory=$true)]
  [string]$vmTemplate,
  [parameter(Mandatory=$true)]
  [string]$targetFolder,
  [parameter(Mandatory=$true)]
  [string]$targetRP,
  [parameter(Mandatory=$true)]
  [string]$vmRootUser,
  [parameter(Mandatory=$true)]
  [string]$vmRootPassword,
  [parameter(Mandatory=$true)]
  [string]$vmUser,
  [parameter(Mandatory=$true)]
  [string]$vmUserPassword 
)

# Copyright (c) 2013 VMware, Inc. All rights reserved.

$ErrorActionPreference = "Stop"

<#
PowerCLI C:\vfra-workspace\vFabricRA2\vfra-build\bootstrap> 
.\invoke_run_vfra.ps1 -viServer vSphereServer -viUser root -viPassword vmware -vmName vfra-devenv-1
 -vmTemplate vfabricra-centos63-base -targetFolder vfra-DevelopmentEnv -targetRP vFRA-DevelopmentEnv -vmRootUser root 
 -vmRootPassword vmware -vmUser vfabric -vmUserPassword vmware
#>

Connect-VIServer -Server $viserver -Password $vipassword -User $viuser
# $vmName = "vfra-devenv-1" 
# $createNew = "true"
#$vfraBaseTemplate = Get-Template -Name "vfra-ovf-template"
$vfraBaseTemplate = Get-Template -Name $vmTemplate
$vfraDevEnvFolder = Get-Folder -Name $targetFolder
$vfraDevEnvRP = Get-ResourcePool -Name $targetRP

New-VM -Name $vmName -ResourcePool $vfraDevEnvRP -Template $vfraBaseTemplate -Location $vfraDevEnvFolder

$vfraVM = Get-VM -Name $vmName
$guest = Get-VMGuest -VM $vfraVM
$guestState = $guest.State
Write-Host "Power State: $guestState" 

# Start the VM and wait for vmtoolsd to come up, because Copy / Invoke doesn't work until it is. 
Start-VM -VM $vfraVM | Wait-Tools 

$guest = Get-VMGuest -VM $vfraVM
$osFullName = $guest.OSFullName
$IPAddress = $guest.IPAddress
$guestState = $guest.State
$guestId = $guest.GuestId
Write-Host "Guest OS: $osFullName"
Write-Host "Guest IP: $IPAddress"
Write-Host "Guest State: $guestState"
Write-Host "Guest GuestID: $guestId"
Write-Host "Sleeping for 30 then checking again..." 

# this should enable the tools to settle down; seems to be a race condition between Wait-Tools and Copy-VMGuestFile
Start-Sleep -s 30

$guest = Get-VMGuest -VM $vfraVM
$osFullName = $guest.OSFullName
$IPAddress = $guest.IPAddress
$guestState = $guest.State
$guestId = $guest.GuestId
Write-Host "Guest OS: $osFullName"
Write-Host "Guest IP: $IPAddress"
Write-Host "Guest State: $guestState"
Write-Host "Guest GuestID: $guestId"

# Uploads and Execute the appropriate vFRA/vfra-build bootstrap scripts. 
Copy-VMGuestFile -Source .\sysprep_centos.sh -Destination "/root" -VM $vfraVM -LocalToGuest -GuestUser $vmRootUser -GuestPassword $vmRootPassword
Invoke-VMScript -ScriptText "chmod 755 /root/sysprep_centos.sh" -VM $vfraVM -GuestUser $vmRootUser -GuestPassword $vmRootPassword
Invoke-VMScript -ScriptText "/root/sysprep_centos.sh" -VM $vfraVM -GuestUser $vmRootUser -GuestPassword $vmRootPassword

Copy-VMGuestFile -Source .\run_vfra_dev.sh -Destination "/home/vfabric" -VM $vfraVM -LocalToGuest -GuestUser $vmUser -GuestPassword $vmUserPassword
Invoke-VMScript -ScriptText "chmod 755 /home/vfabric/run_vfra_dev.sh" -VM $vfraVM -GuestUser $vmUser -GuestPassword $vmUserPassword
Invoke-VMScript -ScriptText "/home/vfabric/run_vfra_dev.sh" -VM $vfraVM -GuestUser $vmUser -GuestPassword $vmUserPassword

Shutdown-VMGuest -VM $vfraVM -Confirm:$false

## Depending on if this is a CI validation, or for publishing, the below are helpful as well:
# Stop-VM -VM $vfraVM -Confirm:$false
# Remove-VM $vfraVM -DeletePermanently -Confirm $false
