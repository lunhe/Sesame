package com.helun.menu.util;

import com.helun.menu.model.BaseEntity;

public interface ISheetFactory {

	ISheetFactory buildSheet(int sheetIndex , String sheetName,BaseEntity sheetData);
}
