package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-13 15:17:58")
/** */
public final class Slim3ModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.Slim3Model> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.Slim3Model, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.Slim3Model, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.Slim3Model> prop1 = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.Slim3Model>(this, "prop1", "prop1");

    private static final Slim3ModelMeta slim3_singleton = new Slim3ModelMeta();

    /**
     * @return the singleton
     */
    public static Slim3ModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public Slim3ModelMeta() {
        super("Slim3Model", jp.co.nemuzuka.model.Slim3Model.class);
    }

    @Override
    public jp.co.nemuzuka.model.Slim3Model entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.Slim3Model model = new jp.co.nemuzuka.model.Slim3Model();
        model.setKey(entity.getKey());
        model.setProp1((java.lang.String) entity.getProperty("prop1"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.Slim3Model m = (jp.co.nemuzuka.model.Slim3Model) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("prop1", m.getProp1());
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.Slim3Model m = (jp.co.nemuzuka.model.Slim3Model) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.Slim3Model m = (jp.co.nemuzuka.model.Slim3Model) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        throw new IllegalStateException("The version property of the model(jp.co.nemuzuka.model.Slim3Model) is not defined.");
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
        jp.co.nemuzuka.model.Slim3Model m = (jp.co.nemuzuka.model.Slim3Model) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getProp1() != null){
            writer.setNextPropertyName("prop1");
            encoder0.encode(writer, m.getProp1());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.Slim3Model jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.Slim3Model m = new jp.co.nemuzuka.model.Slim3Model();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("prop1");
        m.setProp1(decoder0.decode(reader, m.getProp1()));
        return m;
    }
}