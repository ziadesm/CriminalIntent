package com.example.android.criminalintent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.criminalintent.database.CrimeBaseHelper;
import com.example.android.criminalintent.database.CrimeCursorWrapper;
import com.example.android.criminalintent.database.CrimeDBSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.android.criminalintent.database.CrimeDBSchema.CrimeTable.Cols.DATE;
import static com.example.android.criminalintent.database.CrimeDBSchema.CrimeTable.Cols.SOLVED;
import static com.example.android.criminalintent.database.CrimeDBSchema.CrimeTable.Cols.SUSPECT;
import static com.example.android.criminalintent.database.CrimeDBSchema.CrimeTable.Cols.TITLE;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeDBSchema.CrimeTable.NAME, null, values);
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeDBSchema.CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() });
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime) {
        String s = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeDBSchema.CrimeTable.NAME, values
                , CrimeDBSchema.CrimeTable.Cols.UUID + " = ?"
                ,new String[]{ s });
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDBSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(TITLE, crime.getTitle());
        values.put(DATE, crime.getDate().getTime());
        values.put(SOLVED, crime.isSolved() ? 1 : 0);
        values.put(SUSPECT, crime.getSuspect());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereCluse, String[] whereArgs) {
        Cursor query = mDatabase.query(CrimeDBSchema.CrimeTable.NAME
                , null
                , whereCluse
                , whereArgs
                , null
                , null
                , null);
        return new CrimeCursorWrapper(query);
    }
}