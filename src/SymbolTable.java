import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    private Map<String, SymbolEntry> symbols;

    public SymbolTable() {
        this.symbols = new HashMap<>();
    }

    public void addIdentifier(String name, int line, int column) {
        if (symbols.containsKey(name)) {
            // Update frequency
            SymbolEntry entry = symbols.get(name);
            entry.incrementFrequency();
        } else {
            // Add new entry
            symbols.put(name, new SymbolEntry(name, line, column));
        }
    }

    public boolean contains(String name) {
        return symbols.containsKey(name);
    }

    public SymbolEntry getEntry(String name) {
        return symbols.get(name);
    }

    public int getSize() {
        return symbols.size();
    }

    public void print() {
        if (symbols.isEmpty()) {
            System.out.println("Symbol table is empty.");
            return;
        }

        System.out.println("\n=== Symbol Table ===");
        System.out.println(String.format("%-30s %-10s %-15s %-10s", 
                                        "Identifier", "Type", "First Occurrence", "Frequency"));
        System.out.println("=".repeat(75));

        List<SymbolEntry> entries = new ArrayList<>(symbols.values());
        entries.sort((a, b) -> a.name.compareTo(b.name));

        for (SymbolEntry entry : entries) {
            System.out.println(entry);
        }
        
        System.out.println("=".repeat(75));
        System.out.println("Total unique identifiers: " + symbols.size());
    }

    public void clear() {
        symbols.clear();
    }

    // Inner class representing a symbol table entry
    public static class SymbolEntry {
        private String name;
        private String type;
        private int firstLine;
        private int firstColumn;
        private int frequency;

        public SymbolEntry(String name, int line, int column) {
            this.name = name;
            this.type = "IDENTIFIER";
            this.firstLine = line;
            this.firstColumn = column;
            this.frequency = 1;
        }

        public void incrementFrequency() {
            frequency++;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public int getFirstLine() {
            return firstLine;
        }

        public int getFirstColumn() {
            return firstColumn;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return String.format("%-30s %-10s Line: %-4d Col: %-4d  %d", 
                               name, type, firstLine, firstColumn, frequency);
        }
    }
}
