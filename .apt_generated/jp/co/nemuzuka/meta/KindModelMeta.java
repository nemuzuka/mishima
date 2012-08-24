package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-13 15:17:58")
/** */
public final class KindModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.KindModel> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.KindModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.KindModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.KindModel, com.google.appengine.api.datastore.Text> kindName = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.KindModel, com.google.appengine.api.datastore.Text>(this, "kindName", "kindName", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.KindModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.KindModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final KindModelMeta slim3_singleton = new KindModelMeta();

    /**
     * @return the singleton
     */
    public static KindModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public KindModelMeta() {
        super("KindModel", jp.co.nemuzuka.model.KindModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.KindModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.KindModel model = new jp.co.nemuzuka.model.KindModel();
        model.setKey(entity.getKey());
        model.setKindName((com.google.appengine.api.datastore.Text) entity.getProperty("kindName"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.KindModel m = (jp.co.nemuzuka.model.KindModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("kindName", m.getKindName());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.KindModel m = (jp.co.nemuzuka.model.KindModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.KindModel m = (jp.co.nemuzuka.model.KindModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.KindModel m = (jp.co.nemuzuka.model.KindModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.KindModel m = (jp.co.nemuzuka.model.KindModel) model;
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
        jp.co.nemuzuka.model.KindModel m = (jp.co.nemuzuka.model.KindModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getKindName() != null && m.getKindName().getValue() != null){
            writer.setNextPropertyName("kindName");
            encoder0.encode(writer, m.getKindName());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.KindModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.KindModel m = new jp.co.nemuzuka.model.KindModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("kindName");
        m.setKindName(decoder0.decode(reader, m.getKindName()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}