package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-13 15:17:58")
/** */
public final class CommentModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.CommentModel> {

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.CommentModel, com.google.appengine.api.datastore.Text> comment = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.CommentModel, com.google.appengine.api.datastore.Text>(this, "comment", "comment", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, com.google.appengine.api.datastore.Key> createMemberKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, com.google.appengine.api.datastore.Key>(this, "createMemberKey", "createMemberKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, java.util.Date> createdAt = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, java.util.Date>(this, "createdAt", "createdAt", java.util.Date.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, com.google.appengine.api.datastore.Key> refsKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, com.google.appengine.api.datastore.Key>(this, "refsKey", "refsKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.CommentModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final org.slim3.datastore.CreationDate slim3_createdAtAttributeListener = new org.slim3.datastore.CreationDate();

    private static final CommentModelMeta slim3_singleton = new CommentModelMeta();

    /**
     * @return the singleton
     */
    public static CommentModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public CommentModelMeta() {
        super("CommentModel", jp.co.nemuzuka.model.CommentModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.CommentModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.CommentModel model = new jp.co.nemuzuka.model.CommentModel();
        model.setComment((com.google.appengine.api.datastore.Text) entity.getProperty("comment"));
        model.setCreateMemberKey((com.google.appengine.api.datastore.Key) entity.getProperty("createMemberKey"));
        model.setCreatedAt((java.util.Date) entity.getProperty("createdAt"));
        model.setKey(entity.getKey());
        model.setRefsKey((com.google.appengine.api.datastore.Key) entity.getProperty("refsKey"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.CommentModel m = (jp.co.nemuzuka.model.CommentModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setUnindexedProperty("comment", m.getComment());
        entity.setUnindexedProperty("createMemberKey", m.getCreateMemberKey());
        entity.setProperty("createdAt", m.getCreatedAt());
        entity.setProperty("refsKey", m.getRefsKey());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.CommentModel m = (jp.co.nemuzuka.model.CommentModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.CommentModel m = (jp.co.nemuzuka.model.CommentModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.CommentModel m = (jp.co.nemuzuka.model.CommentModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.CommentModel m = (jp.co.nemuzuka.model.CommentModel) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected void prePut(Object model) {
        jp.co.nemuzuka.model.CommentModel m = (jp.co.nemuzuka.model.CommentModel) model;
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
        jp.co.nemuzuka.model.CommentModel m = (jp.co.nemuzuka.model.CommentModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getComment() != null && m.getComment().getValue() != null){
            writer.setNextPropertyName("comment");
            encoder0.encode(writer, m.getComment());
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
        if(m.getRefsKey() != null){
            writer.setNextPropertyName("refsKey");
            encoder0.encode(writer, m.getRefsKey());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.CommentModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.CommentModel m = new jp.co.nemuzuka.model.CommentModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("comment");
        m.setComment(decoder0.decode(reader, m.getComment()));
        reader = rootReader.newObjectReader("createMemberKey");
        m.setCreateMemberKey(decoder0.decode(reader, m.getCreateMemberKey()));
        reader = rootReader.newObjectReader("createdAt");
        m.setCreatedAt(decoder0.decode(reader, m.getCreatedAt()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("refsKey");
        m.setRefsKey(decoder0.decode(reader, m.getRefsKey()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}