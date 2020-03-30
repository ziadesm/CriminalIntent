package com.example.android.criminalintent.database;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.android.criminalintent.Crime;

import java.util.Date;

import static com.example.android.criminalintent.database.CrimeDBSchema.CrimeTable.Cols.DATE;
import static com.example.android.criminalintent.database.CrimeDBSchema.CrimeTable.Cols.SOLVED;
import static com.example.android.criminalintent.database.CrimeDBSchema.CrimeTable.Cols.SUSPECT;
import static com.example.android.criminalintent.database.CrimeDBSchema.CrimeTable.Cols.TITLE;
import static com.example.android.criminalintent.database.CrimeDBSchema.CrimeTable.Cols.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(UUID));
        String title = getString(getColumnIndex(TITLE));
        long date = getLong(getColumnIndex(DATE));
        int isSolved = getInt(getColumnIndex(SOLVED));
        String suspect = getString(getColumnIndex(SUSPECT));

        Crime crime = new Crime(java.util.UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved !=0);
        crime.setSuspect(suspect);
        return crime;
    }
}
