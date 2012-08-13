package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-13 15:17:58")
/** */
public final class TodoModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.TodoModel> {

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.TodoModel, com.google.appengine.api.datastore.Text> content = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.TodoModel, com.google.appengine.api.datastore.Text>(this, "content", "content", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, com.google.appengine.api.datastore.Key> createMemberKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, com.google.appengine.api.datastore.Key>(this, "createMemberKey", "createMemberKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.TodoModel, java.util.Date> createdAt = new org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.TodoModel, java.util.Date>(this, "createdAt", "createdAt", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, java.util.Date> period = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, java.util.Date>(this, "period", "period", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, jp.co.nemuzuka.common.TodoStatus> status = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, jp.co.nemuzuka.common.TodoStatus>(this, "status", "status", jp.co.nemuzuka.common.TodoStatus.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TodoModel> tag = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TodoModel>(this, "tag", "tag");

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TodoModel> title = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TodoModel>(this, "title", "title");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TodoModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final org.slim3.datastore.CreationDate slim3_createdAtAttributeListener = new org.slim3.datastore.CreationDate();

    private static final TodoModelMeta slim3_singleton = new TodoModelMeta();

    /**
     * @return the singleton
     */
    public static TodoModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public TodoModelMeta() {
        super("TodoModel", jp.co.nemuzuka.model.TodoModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.TodoModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.TodoModel model = new jp.co.nemuzuka.model.TodoModel();
        model.setContent((com.google.appengine.api.datastore.Text) entity.getProperty("content"));
        model.setCreateMemberKey((com.google.appengine.api.datastore.Key) entity.getProperty("createMemberKey"));
        model.setCreatedAt((java.util.Date) entity.getProperty("createdAt"));
        model.setKey(entity.getKey());
        model.setPeriod((java.util.Date) entity.getProperty("period"));
        model.setStatus(stringToEnum(jp.co.nemuzuka.common.TodoStatus.class, (java.lang.String) entity.getProperty("status")));
        model.setTag((java.lang.String) entity.getProperty("tag"));
        model.setTitle((java.lang.String) entity.getProperty("title"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.TodoModel m = (jp.co.nemuzuka.model.TodoModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("content", m.getContent());
        entity.setProperty("createMemberKey", m.getCreateMemberKey());
        entity.setUnindexedProperty("createdAt", m.getCreatedAt());
        entity.setProperty("period", m.getPeriod());
        entity.setProperty("status", enumToString(m.getStatus()));
        entity.setProperty("tag", m.getTag());
        entity.setProperty("title", m.getTitle());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.TodoModel m = (jp.co.nemuzuka.model.TodoModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.TodoModel m = (jp.co.nemuzuka.model.TodoModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.TodoModel m = (jp.co.nemuzuka.model.TodoModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.TodoModel m = (jp.co.nemuzuka.model.TodoModel) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected void prePut(Object model) {
        jp.co.nemuzuka.model.TodoModel m = (jp.co.nemuzuka.model.TodoModel) model;
        m.setCreatedAt(slim3_createdAtAttributeListener.prePut(m.getCreatedAt()));
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
        jp.co.nemuzuka.model.TodoModel m = (jp.co.nemuzuka.model.TodoModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getContent() != null && m.getContent().getValue() != null){
            writer.setNextPropertyName("content");
            encoder0.encode(writer, m.getContent());
        }
        if(m.getCreateMemberKey() != null){
            writer.setNextPropertyName("createMemberKey");
            encoder0.encode(writer, m.getCreateMemberKey());
        }
        if(m.getCreatedAt() != null){
            writer.setNextPropertyName("createdAt");
            encoder0.encode(writer, m.getCreatedAt());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getPeriod() != null){
            writer.setNextPropertyName("period");
            encoder0.encode(writer, m.getPeriod());
        }
        if(m.getStatus() != null){
            writer.setNextPropertyName("status");
            encoder0.encode(writer, m.getStatus());
        }
        if(m.getTag() != null){
            writer.setNextPropertyName("tag");
            encoder0.encode(writer, m.getTag());
        }
        if(m.getTitle() != null){
            writer.setNextPropertyName("title");
            encoder0.encode(writer, m.getTitle());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.TodoModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.TodoModel m = new jp.co.nemuzuka.model.TodoModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("content");
        m.setContent(decoder0.decode(reader, m.getContent()));
        reader = rootReader.newObjectReader("createMemberKey");
        m.setCreateMemberKey(decoder0.decode(reader, m.getCreateMemberKey()));
        reader = rootReader.newObjectReader("createdAt");
        m.setCreatedAt(decoder0.decode(reader, m.getCreatedAt()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("period");
        m.setPeriod(decoder0.decode(reader, m.getPeriod()));
        reader = rootReader.newObjectReader("status");
        m.setStatus(decoder0.decode(reader, m.getStatus(), jp.co.nemuzuka.common.TodoStatus.class));
        reader = rootReader.newObjectReader("tag");
        m.setTag(decoder0.decode(reader, m.getTag()));
        reader = rootReader.newObjectReader("title");
        m.setTitle(decoder0.decode(reader, m.getTitle()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}