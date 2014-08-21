package com.neo.prettygirl.event;

public class BroadCastEvent {
	public final static int BASE_EVENT = 0;

	public final static int GET_MAIN_IMAGE_LIST_DATA = BASE_EVENT + 1;
	public final static int GET_ALL_IMAGE_LIST_DATA = BASE_EVENT + 2;

	private int type;
	private Object obj;

	public BroadCastEvent(int type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	public int getType() {
		return type;
	}

	public Object getObject() {
		return obj;
	}
}
