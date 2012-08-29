package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-29 12:29:49")
/** */
public final class MilestoneModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.MilestoneModel> {

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, java.util.Date> endDate = new org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, java.util.Date>(this, "endDate", "endDate", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringUnindexedAttributeMeta<jp.co.nemuzuka.model.MilestoneModel> milestoneName = new org.slim3.datastore.StringUnindexedAttributeMeta<jp.co.nemuzuka.model.MilestoneModel>(this, "milestoneName", "milestoneName");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, com.google.appengine.api.datastore.Key> projectKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, com.google.appengine.api.datastore.Key>(this, "projectKey", "projectKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, java.lang.Long> sortNum = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, java.lang.Long>(this, "sortNum", "sortNum", java.lang.Long.class);

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, java.util.Date> startDate = new org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, java.util.Date>(this, "startDate", "startDate", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MilestoneModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final MilestoneModelMeta slim3_singleton = new MilestoneModelMeta();

    /**
     * @return the singleton
     */
    public static MilestoneModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public MilestoneModelMeta() {
        super("MilestoneModel", jp.co.nemuzuka.model.MilestoneModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.MilestoneModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.MilestoneModel model = new jp.co.nemuzuka.model.MilestoneModel();
        model.setEndDate((java.util.Date) entity.getProperty("endDate"));
        model.setKey(entity.getKey());
        model.setMilestoneName((java.lang.String) entity.getProperty("milestoneName"));
        model.setProjectKey((com.google.appengine.api.datastore.Key) entity.getProperty("projectKey"));
        model.setSortNum((java.lang.Long) entity.getProperty("sortNum"));
        model.setStartDate((java.util.Date) entity.getProperty("startDate"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.MilestoneModel m = (jp.co.nemuzuka.model.MilestoneModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("endDate", m.getEndDate());
        entity.setUnindexedProperty("milestoneName", m.getMilestoneName());
        entity.setProperty("projectKey", m.getProjectKey());
        entity.setProperty("sortNum", m.getSortNum());
        entity.setUnindexedProperty("startDate", m.getStartDate());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.MilestoneModel m = (jp.co.nemuzuka.model.MilestoneModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.MilestoneModel m = (jp.co.nemuzuka.model.MilestoneModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.MilestoneModel m = (jp.co.nemuzuka.model.MilestoneModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.MilestoneModel m = (jp.co.nemuzuka.model.MilestoneModel) model;
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
        jp.co.nemuzuka.model.MilestoneModel m = (jp.co.nemuzuka.model.MilestoneModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getEndDate() != null){
            writer.setNextPropertyName("endDate");
            encoder0.encode(writer, m.getEndDate());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getMilestoneName() != null){
            writer.setNextPropertyName("milestoneName");
            encoder0.encode(writer, m.getMilestoneName());
        }
        if(m.getProjectKey() != null){
            writer.setNextPropertyName("projectKey");
            encoder0.encode(writer, m.getProjectKey());
        }
        if(m.getSortNum() != null){
            writer.setNextPropertyName("sortNum");
            encoder0.encode(writer, m.getSortNum());
        }
        if(m.getStartDate() != null){
            writer.setNextPropertyName("startDate");
            encoder0.encode(writer, m.getStartDate());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.MilestoneModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.MilestoneModel m = new jp.co.nemuzuka.model.MilestoneModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("endDate");
        m.setEndDate(decoder0.decode(reader, m.getEndDate()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("milestoneName");
        m.setMilestoneName(decoder0.decode(reader, m.getMilestoneName()));
        reader = rootReader.newObjectReader("projectKey");
        m.setProjectKey(decoder0.decode(reader, m.getProjectKey()));
        reader = rootReader.newObjectReader("sortNum");
        m.setSortNum(decoder0.decode(reader, m.getSortNum()));
        reader = rootReader.newObjectReader("startDate");
        m.setStartDate(decoder0.decode(reader, m.getStartDate()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}