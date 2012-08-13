package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-13 15:17:58")
/** */
public final class ProjectModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.ProjectModel> {

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.ProjectModel> projectId = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.ProjectModel>(this, "projectId", "projectId");

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.ProjectModel> projectName = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.ProjectModel>(this, "projectName", "projectName");

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.ProjectModel, com.google.appengine.api.datastore.Text> projectSummary = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.ProjectModel, com.google.appengine.api.datastore.Text>(this, "projectSummary", "projectSummary", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.ProjectModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final ProjectModelMeta slim3_singleton = new ProjectModelMeta();

    /**
     * @return the singleton
     */
    public static ProjectModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public ProjectModelMeta() {
        super("ProjectModel", jp.co.nemuzuka.model.ProjectModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.ProjectModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.ProjectModel model = new jp.co.nemuzuka.model.ProjectModel();
        model.setKey(entity.getKey());
        model.setProjectId((java.lang.String) entity.getProperty("projectId"));
        model.setProjectName((java.lang.String) entity.getProperty("projectName"));
        model.setProjectSummary((com.google.appengine.api.datastore.Text) entity.getProperty("projectSummary"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.ProjectModel m = (jp.co.nemuzuka.model.ProjectModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("projectId", m.getProjectId());
        entity.setProperty("projectName", m.getProjectName());
        entity.setUnindexedProperty("projectSummary", m.getProjectSummary());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.ProjectModel m = (jp.co.nemuzuka.model.ProjectModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.ProjectModel m = (jp.co.nemuzuka.model.ProjectModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.ProjectModel m = (jp.co.nemuzuka.model.ProjectModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.ProjectModel m = (jp.co.nemuzuka.model.ProjectModel) model;
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
        jp.co.nemuzuka.model.ProjectModel m = (jp.co.nemuzuka.model.ProjectModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getProjectId() != null){
            writer.setNextPropertyName("projectId");
            encoder0.encode(writer, m.getProjectId());
        }
        if(m.getProjectName() != null){
            writer.setNextPropertyName("projectName");
            encoder0.encode(writer, m.getProjectName());
        }
        if(m.getProjectSummary() != null && m.getProjectSummary().getValue() != null){
            writer.setNextPropertyName("projectSummary");
            encoder0.encode(writer, m.getProjectSummary());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.ProjectModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.ProjectModel m = new jp.co.nemuzuka.model.ProjectModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("projectId");
        m.setProjectId(decoder0.decode(reader, m.getProjectId()));
        reader = rootReader.newObjectReader("projectName");
        m.setProjectName(decoder0.decode(reader, m.getProjectName()));
        reader = rootReader.newObjectReader("projectSummary");
        m.setProjectSummary(decoder0.decode(reader, m.getProjectSummary()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}