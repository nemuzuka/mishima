package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-29 12:29:49")
/** */
public final class MemberModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.MemberModel> {

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.MemberModel, jp.co.nemuzuka.common.Authority> authority = new org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.MemberModel, jp.co.nemuzuka.common.Authority>(this, "authority", "authority", jp.co.nemuzuka.common.Authority.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MemberModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MemberModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.MemberModel> mail = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.MemberModel>(this, "mail", "mail");

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.MemberModel, com.google.appengine.api.datastore.Text> memo = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.MemberModel, com.google.appengine.api.datastore.Text>(this, "memo", "memo", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.MemberModel> name = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.MemberModel>(this, "name", "name");

    /** */
    public final org.slim3.datastore.StringUnindexedAttributeMeta<jp.co.nemuzuka.model.MemberModel> timeZone = new org.slim3.datastore.StringUnindexedAttributeMeta<jp.co.nemuzuka.model.MemberModel>(this, "timeZone", "timeZone");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MemberModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.MemberModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final MemberModelMeta slim3_singleton = new MemberModelMeta();

    /**
     * @return the singleton
     */
    public static MemberModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public MemberModelMeta() {
        super("MemberModel", jp.co.nemuzuka.model.MemberModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.MemberModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.MemberModel model = new jp.co.nemuzuka.model.MemberModel();
        model.setAuthority(stringToEnum(jp.co.nemuzuka.common.Authority.class, (java.lang.String) entity.getProperty("authority")));
        model.setKey(entity.getKey());
        model.setMail((java.lang.String) entity.getProperty("mail"));
        model.setMemo((com.google.appengine.api.datastore.Text) entity.getProperty("memo"));
        model.setName((java.lang.String) entity.getProperty("name"));
        model.setTimeZone((java.lang.String) entity.getProperty("timeZone"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.MemberModel m = (jp.co.nemuzuka.model.MemberModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("authority", enumToString(m.getAuthority()));
        entity.setProperty("mail", m.getMail());
        entity.setUnindexedProperty("memo", m.getMemo());
        entity.setProperty("name", m.getName());
        entity.setUnindexedProperty("timeZone", m.getTimeZone());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.MemberModel m = (jp.co.nemuzuka.model.MemberModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.MemberModel m = (jp.co.nemuzuka.model.MemberModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.MemberModel m = (jp.co.nemuzuka.model.MemberModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.MemberModel m = (jp.co.nemuzuka.model.MemberModel) model;
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
        jp.co.nemuzuka.model.MemberModel m = (jp.co.nemuzuka.model.MemberModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getAuthority() != null){
            writer.setNextPropertyName("authority");
            encoder0.encode(writer, m.getAuthority());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getMail() != null){
            writer.setNextPropertyName("mail");
            encoder0.encode(writer, m.getMail());
        }
        if(m.getMemo() != null && m.getMemo().getValue() != null){
            writer.setNextPropertyName("memo");
            encoder0.encode(writer, m.getMemo());
        }
        if(m.getName() != null){
            writer.setNextPropertyName("name");
            encoder0.encode(writer, m.getName());
        }
        if(m.getTimeZone() != null){
            writer.setNextPropertyName("timeZone");
            encoder0.encode(writer, m.getTimeZone());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.MemberModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.MemberModel m = new jp.co.nemuzuka.model.MemberModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("authority");
        m.setAuthority(decoder0.decode(reader, m.getAuthority(), jp.co.nemuzuka.common.Authority.class));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("mail");
        m.setMail(decoder0.decode(reader, m.getMail()));
        reader = rootReader.newObjectReader("memo");
        m.setMemo(decoder0.decode(reader, m.getMemo()));
        reader = rootReader.newObjectReader("name");
        m.setName(decoder0.decode(reader, m.getName()));
        reader = rootReader.newObjectReader("timeZone");
        m.setTimeZone(decoder0.decode(reader, m.getTimeZone()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}