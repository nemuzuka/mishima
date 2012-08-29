package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-29 12:29:48")
/** */
public final class UploadFileModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.UploadFileModel> {

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.UploadFileModel> blobKey = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.UploadFileModel>(this, "blobKey", "blobKey");

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, com.google.appengine.api.datastore.Text> comment = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, com.google.appengine.api.datastore.Text>(this, "comment", "comment", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.UploadFileModel> contentType = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.UploadFileModel>(this, "contentType", "contentType");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, java.util.Date> creation = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, java.util.Date>(this, "creation", "creation", java.util.Date.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.UploadFileModel> filename = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.UploadFileModel>(this, "filename", "filename");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.UploadFileModel> md5Hash = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.UploadFileModel>(this, "md5Hash", "md5Hash");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, com.google.appengine.api.datastore.Key> parentKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, com.google.appengine.api.datastore.Key>(this, "parentKey", "parentKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, com.google.appengine.api.datastore.Key> projectKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, com.google.appengine.api.datastore.Key>(this, "projectKey", "projectKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, java.lang.Long> size = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, java.lang.Long>(this, "size", "size", java.lang.Long.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.UploadFileModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final UploadFileModelMeta slim3_singleton = new UploadFileModelMeta();

    /**
     * @return the singleton
     */
    public static UploadFileModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public UploadFileModelMeta() {
        super("UploadFileModel", jp.co.nemuzuka.model.UploadFileModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.UploadFileModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.UploadFileModel model = new jp.co.nemuzuka.model.UploadFileModel();
        model.setBlobKey((java.lang.String) entity.getProperty("blobKey"));
        model.setComment((com.google.appengine.api.datastore.Text) entity.getProperty("comment"));
        model.setContentType((java.lang.String) entity.getProperty("contentType"));
        model.setCreation((java.util.Date) entity.getProperty("creation"));
        model.setFilename((java.lang.String) entity.getProperty("filename"));
        model.setKey(entity.getKey());
        model.setMd5Hash((java.lang.String) entity.getProperty("md5Hash"));
        model.setParentKey((com.google.appengine.api.datastore.Key) entity.getProperty("parentKey"));
        model.setProjectKey((com.google.appengine.api.datastore.Key) entity.getProperty("projectKey"));
        model.setSize((java.lang.Long) entity.getProperty("size"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.UploadFileModel m = (jp.co.nemuzuka.model.UploadFileModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("blobKey", m.getBlobKey());
        entity.setUnindexedProperty("comment", m.getComment());
        entity.setProperty("contentType", m.getContentType());
        entity.setProperty("creation", m.getCreation());
        entity.setProperty("filename", m.getFilename());
        entity.setProperty("md5Hash", m.getMd5Hash());
        entity.setProperty("parentKey", m.getParentKey());
        entity.setProperty("projectKey", m.getProjectKey());
        entity.setProperty("size", m.getSize());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.UploadFileModel m = (jp.co.nemuzuka.model.UploadFileModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.UploadFileModel m = (jp.co.nemuzuka.model.UploadFileModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.UploadFileModel m = (jp.co.nemuzuka.model.UploadFileModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.UploadFileModel m = (jp.co.nemuzuka.model.UploadFileModel) model;
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
        jp.co.nemuzuka.model.UploadFileModel m = (jp.co.nemuzuka.model.UploadFileModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getBlobKey() != null){
            writer.setNextPropertyName("blobKey");
            encoder0.encode(writer, m.getBlobKey());
        }
        if(m.getComment() != null && m.getComment().getValue() != null){
            writer.setNextPropertyName("comment");
            encoder0.encode(writer, m.getComment());
        }
        if(m.getContentType() != null){
            writer.setNextPropertyName("contentType");
            encoder0.encode(writer, m.getContentType());
        }
        if(m.getCreation() != null){
            writer.setNextPropertyName("creation");
            encoder0.encode(writer, m.getCreation());
        }
        if(m.getFilename() != null){
            writer.setNextPropertyName("filename");
            encoder0.encode(writer, m.getFilename());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getMd5Hash() != null){
            writer.setNextPropertyName("md5Hash");
            encoder0.encode(writer, m.getMd5Hash());
        }
        if(m.getParentKey() != null){
            writer.setNextPropertyName("parentKey");
            encoder0.encode(writer, m.getParentKey());
        }
        if(m.getProjectKey() != null){
            writer.setNextPropertyName("projectKey");
            encoder0.encode(writer, m.getProjectKey());
        }
        if(m.getSize() != null){
            writer.setNextPropertyName("size");
            encoder0.encode(writer, m.getSize());
        }
        if(m.getVersion() != null){
            writer.setNextPropertyName("version");
            encoder0.encode(writer, m.getVersion());
        }
        writer.endObject();
    }

    @Override
    protected jp.co.nemuzuka.model.UploadFileModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.UploadFileModel m = new jp.co.nemuzuka.model.UploadFileModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("blobKey");
        m.setBlobKey(decoder0.decode(reader, m.getBlobKey()));
        reader = rootReader.newObjectReader("comment");
        m.setComment(decoder0.decode(reader, m.getComment()));
        reader = rootReader.newObjectReader("contentType");
        m.setContentType(decoder0.decode(reader, m.getContentType()));
        reader = rootReader.newObjectReader("creation");
        m.setCreation(decoder0.decode(reader, m.getCreation()));
        reader = rootReader.newObjectReader("filename");
        m.setFilename(decoder0.decode(reader, m.getFilename()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("md5Hash");
        m.setMd5Hash(decoder0.decode(reader, m.getMd5Hash()));
        reader = rootReader.newObjectReader("parentKey");
        m.setParentKey(decoder0.decode(reader, m.getParentKey()));
        reader = rootReader.newObjectReader("projectKey");
        m.setProjectKey(decoder0.decode(reader, m.getProjectKey()));
        reader = rootReader.newObjectReader("size");
        m.setSize(decoder0.decode(reader, m.getSize()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}