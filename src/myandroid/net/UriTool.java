package myandroid.net;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.MediaColumns;

public class UriTool {
	
	public UriTool() {
		// TODO Auto-generated constructor stub
	}

	public String toPath(Context context, android.net.Uri uri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaColumns.DATA };
			cursor = context.getContentResolver().query(uri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
}
