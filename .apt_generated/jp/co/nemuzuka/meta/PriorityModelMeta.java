package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-13 15:17:58")
/** */
public final class PriorityModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.PriorityModel> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.PriorityModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.PriorityModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.PriorityModel, com.google.appengine.api.datastore.Text> priorityName = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.PriorityModel, com.google.appengine.api.datastore.Text>(this, "priorityName", "priorityName", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.PriorityModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.PriorityModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final PriorityModelMeta slim3_singleton = new PriorityModelMeta();

    /**
     * @return the singleton
     */
    public static PriorityModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public PriorityModelMeta() {
        super("PriorityModel", jp.co.nemuzuka.model.PriorityModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.PriorityModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.PriorityModel model = new jp.co.nemuzuka.model.PriorityModel();
        model.setKey(entity.getKey());
        model.setPriorityName((com.google.appengine.api.datastore.Text) entity.getProperty("priorityName"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.PriorityModel m = (jp.co.nemuzuka.model.PriorityModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("priorityName", m.getPriorityName());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.PriorityModel m = (jp.co.nemuzuka.model.PriorityModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.PriorityModel m = (jp.co.nemuzuka.model.PriorityModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.PriorityModel m = (jp.co.nemuzuka.model.PriorityModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.PriorityModel m = (jp.co.nemuzuka.model.PriorityModel) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected void prePut(Object model) {
    }

    @Override
    protected void postGet(Object model) {
    }

    @Override
    public String getSchemaVersionName() {
        return "slim3.schemaVersion";
    }

    @Override
    public String getClassHierarchyListName() {
        return "slim3.classHierarchyList";
    }

    @Override
    protected boolean isCipherProperty(String propertyName) {
        return false;
    }

    @Override
    protected void modelToJson(org.slim3.datastore.json.JsonWriter writer, java.lang.Object model, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.PriorityModel m = (jp.co.nemuzuka.model.PriorityModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getPriorityName() != null && m.getPriorityName().getValue() != null){
            writer.setNextPropertyName("priorityName");
            encoder0.encode(writer, m.getPriorityName());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.PriorityModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.PriorityModel m = new jp.co.nemuzuka.model.PriorityModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("priorityName");
        m.setPriorityName(decoder0.decode(reader, m.getPriorityName()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}