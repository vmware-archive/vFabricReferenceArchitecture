package com.vmware.vfra.batch.report.support;

import java.util.Date;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import com.vmware.vfra.batch.report.domain.Order;

public class OrderKeyConversionService implements ConversionService {

	@Override
	public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
		if (sourceType == String.class && targetType == Order.Key.class) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Object source, Class<T> targetType) {
		String str = (String) source;
		int i = str.lastIndexOf(':');
		if (i == -1) {
			throw new IllegalArgumentException(
					"can't convert string to Order.Key: missing colon: " + str);
		}
		String id = str.substring(0, i);
		String ts = str.substring(i + 1, str.length());
		Order.Key key = new Order.Key();
		key.orderId = Integer.parseInt(id);
		key.jobTimeStamp = new Date(Long.parseLong(ts));
		return (T) key;
	}

	@Override
	public boolean canConvert(TypeDescriptor sourceType,
			TypeDescriptor targetType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType,
			TypeDescriptor targetType) {
		// TODO Auto-generated method stub
		return null;
	}
}
