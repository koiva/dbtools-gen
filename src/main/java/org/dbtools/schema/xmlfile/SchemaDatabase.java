package org.dbtools.schema.xmlfile;

import org.dbtools.schema.ClassInfo;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Root
public class SchemaDatabase {
    @Attribute
    private String name;

    @Element(required = false)
    private PostSQLScriptFile postSQLScriptFile;

    @ElementList(entry = "table", inline = true)
    private List<SchemaTable> tables = new ArrayList<>();

    @ElementList(entry = "view", inline = true, required = false)
    private List<SchemaView> views = new ArrayList<>();

    public SchemaDatabase() {
    }

    public SchemaDatabase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SchemaTable> getTables() {
        return tables;
    }

    public void setTables(List<SchemaTable> tables) {
        this.tables = tables;
    }

    public List<SchemaView> getViews() {
        return views;
    }

    public void setViews(List<SchemaView> views) {
        this.views = views;
    }

    public List<String> getTableNames() {
        List<String> names = new ArrayList<>();
        for (SchemaTable table : tables) {
            names.add(table.getName());
        }

        return names;
    }

    public List<String> getViewNames() {
        List<String> names = new ArrayList<>();
        for (SchemaView view : views) {
            names.add(view.getName());
        }

        return names;
    }

    /**
     * Case insensitive search for table
     */
    public SchemaTable getTable(String tableName) {
        for (SchemaTable table : tables) {
            if (table.getName().equalsIgnoreCase(tableName)) {
                return table;
            }
        }

        return null;
    }

    /**
     * Case insensitive search for views
     */
    public SchemaView getView(String viewName) {
        for (SchemaView view : views) {
            if (view.getName().equalsIgnoreCase(viewName)) {
                return view;
            }
        }

        return null;
    }

    public ClassInfo getTableClassInfo(String tableName) {
        String className = ClassInfo.createJavaStyleName(tableName);
        return new ClassInfo(className, null);
    }

    public boolean validate() {
        // Check for duplicate table names
        Set<String> existingTableViewNames = new HashSet<>();
        Set<String> existingSequences = new HashSet<>();
        for (SchemaTable table : tables) {
            // table self validation
            table.validate();

            // Check for duplicate table names
            String tableName = table.getName();
            if (existingTableViewNames.contains(tableName)) {
                throw new IllegalStateException("Table named [" + tableName + "] already exists in database [" + getName() + "]");
            }
            existingTableViewNames.add(tableName);

            // Check for duplicate sequence name
            for (String seqName : table.getSequenceNames()) {
                if (existingSequences.contains(seqName)) {
                    throw new IllegalStateException("Sequencer named [" + seqName + "] already exists in database [" + getName() + "]");
                }
                existingSequences.add(seqName);
            }
        }

        // Check for duplicate view names
        for (String viewName : getViewNames()) {
            if (existingTableViewNames.contains(viewName)) {
                throw new IllegalStateException("View named [" + viewName + "] already exists in database [" + getName() + "]");
            }
            existingTableViewNames.add(viewName);
        }

        return true;
    }
}
