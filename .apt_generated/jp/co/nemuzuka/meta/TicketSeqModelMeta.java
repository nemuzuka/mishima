package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-13 15:17:58")
/** */
public final class TicketSeqModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.TicketSeqModel> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketSeqModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketSeqModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketSeqModel, java.lang.Long> no = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketSeqModel, java.lang.Long>(this, "no", "no", java.lang.Long.class);

    private static final TicketSeqModelMeta slim3_singleton = new TicketSeqModelMeta();

    /**
     * @return the singleton
     */
    public static TicketSeqModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public TicketSeqModelMeta() {
        super("TicketSeqModel", jp.co.nemuzuka.model.TicketSeqModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.TicketSeqModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.TicketSeqModel model = new jp.co.nemuzuka.model.TicketSeqModel();
        model.setKey(entity.getKey());
        model.setNo((java.lang.Long) entity.getProperty("no"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.TicketSeqModel m = (jp.co.nemuzuka.model.TicketSeqModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("no", m.getNo());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.TicketSeqModel m = (jp.co.nemuzuka.model.TicketSeqModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.TicketSeqModel m = (jp.co.nemuzuka.model.TicketSeqModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        throw new IllegalStateException("The version property of the model(jp.co.nemuzuka.model.TicketSeqModel) is not defined.");
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
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
        jp.co.nemuzuka.model.TicketSeqModel m = (jp.co.nemuzuka.model.TicketSeqModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getNo() != null){
            writer.setNextPropertyName("no");
            encoder0.encode(writer, m.getNo());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.TicketSeqModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.TicketSeqModel m = new jp.co.nemuzuka.model.TicketSeqModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("no");
        m.setNo(decoder0.decode(reader, m.getNo()));
        return m;
    }
}