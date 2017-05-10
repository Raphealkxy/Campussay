package com.campussay.util;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class ComparatorRecord<T> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		// TODO Auto-generated method stub
		int flag1=0,flag2=0;
		HashMap mp1=(HashMap)o1;
		HashMap mp2=(HashMap)o2;
		flag1=((Date) mp1.get("time")).compareTo((Date)mp2.get("time"));
		if(flag1>0){
			flag2=-1;
		}else{
			flag2=1;
		}
		return flag2;
		
	}

}
