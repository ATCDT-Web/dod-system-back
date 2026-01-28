package com.example.database.parser;

public class CellReferenceHelper {
    public static Integer extractSheetNumber(String raw) {
        if (raw == null) return null;
        String digits = raw.replaceAll("\\D+", "");
        if (digits.isEmpty()) return null;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static CellReference parse(String cellRef) {
        if (cellRef == null) return null;
        String[] parts = cellRef.replaceAll("\\s+", "").split("(?<=\\D)(?=\\d)");
        if (parts.length != 2) return null;
        String letters = parts[0];
        String digits = parts[1];
        int row;
        try {
            row = Integer.parseInt(digits) - 1;
        } catch (NumberFormatException e) {
            return null;
        }
        int col = 0;
        for (char ch : letters.toUpperCase().toCharArray()) {
            if (ch < 'A' || ch > 'Z') return null;
            col = col * 26 + (ch - 'A') + 1;
        }
        return new CellReference(row, col - 1);
    }

    public static class CellReference {
        private final int row;
        private final int col;

        public CellReference(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
}
