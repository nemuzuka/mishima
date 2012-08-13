package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-13 15:17:58")
/** */
public final class ProjectMemberModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.ProjectMemberModel> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, com.google.appengine.api.datastore.Key> memberKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, com.google.appengine.api.datastore.Key>(this, "memberKey", "memberKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, jp.co.nemuzuka.common.ProjectAuthority> projectAuthority = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, jp.co.nemuzuka.common.ProjectAuthority>(this, "projectAuthority", "projectAuthority", jp.co.nemuzuka.common.ProjectAuthority.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, com.google.appengine.api.datastore.Key> projectKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, com.google.appengine.api.datastore.Key>(this, "projectKey", "projectKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectMemberModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final ProjectMemberModelMeta slim3_singleton = new ProjectMemberModelMeta();

    /**
     * @return the singleton
     */
    public static ProjectMemberModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public ProjectMemberModelMeta() {
        super("ProjectMemberModel", jp.co.nemuzuka.model.ProjectMemberModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.ProjectMemberModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.ProjectMemberModel model = new jp.co.nemuzuka.model.ProjectMemberModel();
        model.setKey(entity.getKey());
        model.setMemberKey((com.google.appengine.api.datastore.Key) entity.getProperty("memberKey"));
        model.setProjectAuthority(stringToEnum(jp.co.nemuzuka.common.ProjectAuthority.class, (java.lang.String) entity.getProperty("projectAuthority")));
        model.setProjectKey((com.google.appengine.api.datastore.Key) entity.getProperty("projectKey"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.ProjectMemberModel m = (jp.co.nemuzuka.model.ProjectMemberModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("memberKey", m.getMemberKey());
        entity.setProperty("projectAuthority", enumToString(m.getProjectAuthority()));
        entity.setProperty("projectKey", m.getProjectKey());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.ProjectMemberModel m = (jp.co.nemuzuka.model.ProjectMemberModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.ProjectMemberModel m = (jp.co.nemuzuka.model.ProjectMemberModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.ProjectMemberModel m = (jp.co.nemuzuka.model.ProjectMemberModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.ProjectMemberModel m = (jp.co.nemuzuka.model.ProjectMemberModel) model;
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
        jp.co.nemuzuka.model.ProjectMemberModel m = (jp.co.nemuzuka.model.ProjectMemberModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getMemberKey() != null){
            writer.setNextPropertyName("memberKey");
            encoder0.encode(writer, m.getMemberKey());
        }
        if(m.getProjectAuthority() != null){
            writer.setNextPropertyName("projectAuthority");
            encoder0.encode(writer, m.getProjectAuthority());
        }
        if(m.getProjectKey() != null){
            writer.setNextPropertyName("projectKey");
            encoder0.encode(writer, m.getProjectKey());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.ProjectMemberModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.ProjectMemberModel m = new jp.co.nemuzuka.model.ProjectMemberModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("memberKey");
        m.setMemberKey(decoder0.decode(reader, m.getMemberKey()));
        reader = rootReader.newObjectReader("projectAuthority");
        m.setProjectAuthority(decoder0.decode(reader, m.getProjectAuthority(), jp.co.nemuzuka.common.ProjectAuthority.class));
        reader = rootReader.newObjectReader("projectKey");
        m.setProjectKey(decoder0.decode(reader, m.getProjectKey()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}