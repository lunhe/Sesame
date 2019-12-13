package com.helun.menu.api;

import java.util.Date;

import com.helun.menu.model.BaseEntity;

public interface TaskBOFactory {
	BaseEntity buid(Long userId, Date applyTime,String... data );
}
