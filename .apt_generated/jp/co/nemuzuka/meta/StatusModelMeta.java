package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-29 12:29:48")
/** */
public final class StatusModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.StatusModel> {

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.StatusModel, com.google.appengine.api.datastore.Text> closeStatusName = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.StatusModel, com.google.appengine.api.datastore.Text>(this, "closeStatusName", "closeStatusName", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.StatusModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.StatusModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.StatusModel, com.google.appengine.api.datastore.Text> statusName = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.StatusModel, com.google.appengine.api.datastore.Text>(this, "statusName", "statusName", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.StatusModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.StatusModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final StatusModelMeta slim3_singleton = new StatusModelMeta();

    /**
     * @return the singleton
     */
    public static StatusModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public StatusModelMeta() {
        super("StatusModel", jp.co.nemuzuka.model.StatusModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.StatusModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.StatusModel model = new jp.co.nemuzuka.model.StatusModel();
        model.setCloseStatusName((com.google.appengine.api.datastore.Text) entity.getProperty("closeStatusName"));
        model.setKey(entity.getKey());
        model.setStatusName((com.google.appengine.api.datastore.Text) entity.getProperty("statusName"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.StatusModel m = (jp.co.nemuzuka.model.StatusModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("closeStatusName", m.getCloseStatusName());
        entity.setUnindexedProperty("statusName", m.getStatusName());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.StatusModel m = (jp.co.nemuzuka.model.StatusModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.StatusModel m = (jp.co.nemuzuka.model.StatusModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.StatusModel m = (jp.co.nemuzuka.model.StatusModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.StatusModel m = (jp.co.nemuzuka.model.StatusModel) model;
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
        jp.co.nemuzuka.model.StatusModel m = (jp.co.nemuzuka.model.StatusModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getCloseStatusName() != null && m.getCloseStatusName().getValue() != null){
            writer.setNextPropertyName("closeStatusName");
            encoder0.encode(writer, m.getCloseStatusName());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getStatusName() != null && m.getStatusName().getValue() != null){
            writer.setNextPropertyName("statusName");
            encoder0.encode(writer, m.getStatusName());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.StatusModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.StatusModel m = new jp.co.nemuzuka.model.StatusModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("closeStatusName");
        m.setCloseStatusName(decoder0.decode(reader, m.getCloseStatusName()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("statusName");
        m.setStatusName(decoder0.decode(reader, m.getStatusName()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}