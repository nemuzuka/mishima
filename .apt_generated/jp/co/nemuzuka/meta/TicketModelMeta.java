package jp.co.nemuzuka.meta;

//@javax.annotation.Generated(value = { "slim3-gen", "@VERSION@" }, date = "2012-08-13 15:17:58")
/** */
public final class TicketModelMeta extends org.slim3.datastore.ModelMeta<jp.co.nemuzuka.model.TicketModel> {

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel> category = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel>(this, "category", "category");

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Text> content = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Text>(this, "content", "content", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.util.Date> createdAt = new org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.util.Date>(this, "createdAt", "createdAt", java.util.Date.class);

    /** */
    public final org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Text> endCondition = new org.slim3.datastore.UnindexedAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Text>(this, "endCondition", "endCondition", com.google.appengine.api.datastore.Text.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key> key = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key>(this, "__key__", "key", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key> milestone = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key>(this, "milestone", "milestone", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.lang.Long> no = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.lang.Long>(this, "no", "no", java.lang.Long.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key> parentTicketKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key>(this, "parentTicketKey", "parentTicketKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.util.Date> period = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.util.Date>(this, "period", "period", java.util.Date.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel> priority = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel>(this, "priority", "priority");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key> projectKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key>(this, "projectKey", "projectKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.util.Date> startDate = new org.slim3.datastore.CoreUnindexedAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.util.Date>(this, "startDate", "startDate", java.util.Date.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel> status = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel>(this, "status", "status");

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel> targetKind = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel>(this, "targetKind", "targetKind");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key> targetMemberKey = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, com.google.appengine.api.datastore.Key>(this, "targetMemberKey", "targetMemberKey", com.google.appengine.api.datastore.Key.class);

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel> targetVersion = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel>(this, "targetVersion", "targetVersion");

    /** */
    public final org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel> title = new org.slim3.datastore.StringAttributeMeta<jp.co.nemuzuka.model.TicketModel>(this, "title", "title");

    /** */
    public final org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.lang.Long> version = new org.slim3.datastore.CoreAttributeMeta<jp.co.nemuzuka.model.TicketModel, java.lang.Long>(this, "version", "version", java.lang.Long.class);

    private static final org.slim3.datastore.CreationDate slim3_createdAtAttributeListener = new org.slim3.datastore.CreationDate();

    private static final TicketModelMeta slim3_singleton = new TicketModelMeta();

    /**
     * @return the singleton
     */
    public static TicketModelMeta get() {
       return slim3_singleton;
    }

    /** */
    public TicketModelMeta() {
        super("TicketModel", jp.co.nemuzuka.model.TicketModel.class);
    }

    @Override
    public jp.co.nemuzuka.model.TicketModel entityToModel(com.google.appengine.api.datastore.Entity entity) {
        jp.co.nemuzuka.model.TicketModel model = new jp.co.nemuzuka.model.TicketModel();
        model.setCategory((java.lang.String) entity.getProperty("category"));
        model.setContent((com.google.appengine.api.datastore.Text) entity.getProperty("content"));
        model.setCreatedAt((java.util.Date) entity.getProperty("createdAt"));
        model.setEndCondition((com.google.appengine.api.datastore.Text) entity.getProperty("endCondition"));
        model.setKey(entity.getKey());
        model.setMilestone((com.google.appengine.api.datastore.Key) entity.getProperty("milestone"));
        model.setNo((java.lang.Long) entity.getProperty("no"));
        model.setParentTicketKey((com.google.appengine.api.datastore.Key) entity.getProperty("parentTicketKey"));
        model.setPeriod((java.util.Date) entity.getProperty("period"));
        model.setPriority((java.lang.String) entity.getProperty("priority"));
        model.setProjectKey((com.google.appengine.api.datastore.Key) entity.getProperty("projectKey"));
        model.setStartDate((java.util.Date) entity.getProperty("startDate"));
        model.setStatus((java.lang.String) entity.getProperty("status"));
        model.setTargetKind((java.lang.String) entity.getProperty("targetKind"));
        model.setTargetMemberKey((com.google.appengine.api.datastore.Key) entity.getProperty("targetMemberKey"));
        model.setTargetVersion((java.lang.String) entity.getProperty("targetVersion"));
        model.setTitle((java.lang.String) entity.getProperty("title"));
        model.setVersion((java.lang.Long) entity.getProperty("version"));
        return model;
    }

    @Override
    public com.google.appengine.api.datastore.Entity modelToEntity(java.lang.Object model) {
        jp.co.nemuzuka.model.TicketModel m = (jp.co.nemuzuka.model.TicketModel) model;
        com.google.appengine.api.datastore.Entity entity = null;
        if (m.getKey() != null) {
            entity = new com.google.appengine.api.datastore.Entity(m.getKey());
        } else {
            entity = new com.google.appengine.api.datastore.Entity(kind);
        }
        entity.setProperty("category", m.getCategory());
        entity.setUnindexedProperty("content", m.getContent());
        entity.setUnindexedProperty("createdAt", m.getCreatedAt());
        entity.setUnindexedProperty("endCondition", m.getEndCondition());
        entity.setProperty("milestone", m.getMilestone());
        entity.setProperty("no", m.getNo());
        entity.setProperty("parentTicketKey", m.getParentTicketKey());
        entity.setProperty("period", m.getPeriod());
        entity.setProperty("priority", m.getPriority());
        entity.setProperty("projectKey", m.getProjectKey());
        entity.setUnindexedProperty("startDate", m.getStartDate());
        entity.setProperty("status", m.getStatus());
        entity.setProperty("targetKind", m.getTargetKind());
        entity.setProperty("targetMemberKey", m.getTargetMemberKey());
        entity.setProperty("targetVersion", m.getTargetVersion());
        entity.setProperty("title", m.getTitle());
        entity.setProperty("version", m.getVersion());
        entity.setProperty("slim3.schemaVersion", 1);
        return entity;
    }

    @Override
    protected com.google.appengine.api.datastore.Key getKey(Object model) {
        jp.co.nemuzuka.model.TicketModel m = (jp.co.nemuzuka.model.TicketModel) model;
        return m.getKey();
    }

    @Override
    protected void setKey(Object model, com.google.appengine.api.datastore.Key key) {
        validateKey(key);
        jp.co.nemuzuka.model.TicketModel m = (jp.co.nemuzuka.model.TicketModel) model;
        m.setKey(key);
    }

    @Override
    protected long getVersion(Object model) {
        jp.co.nemuzuka.model.TicketModel m = (jp.co.nemuzuka.model.TicketModel) model;
        return m.getVersion() != null ? m.getVersion().longValue() : 0L;
    }

    @Override
    protected void assignKeyToModelRefIfNecessary(com.google.appengine.api.datastore.AsyncDatastoreService ds, java.lang.Object model) {
    }

    @Override
    protected void incrementVersion(Object model) {
        jp.co.nemuzuka.model.TicketModel m = (jp.co.nemuzuka.model.TicketModel) model;
        long version = m.getVersion() != null ? m.getVersion().longValue() : 0L;
        m.setVersion(Long.valueOf(version + 1L));
    }

    @Override
    protected void prePut(Object model) {
        jp.co.nemuzuka.model.TicketModel m = (jp.co.nemuzuka.model.TicketModel) model;
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
        jp.co.nemuzuka.model.TicketModel m = (jp.co.nemuzuka.model.TicketModel) model;
        writer.beginObject();
        org.slim3.datastore.json.Default encoder0 = new org.slim3.datastore.json.Default();
        if(m.getCategory() != null){
            writer.setNextPropertyName("category");
            encoder0.encode(writer, m.getCategory());
        }
        if(m.getContent() != null && m.getContent().getValue() != null){
            writer.setNextPropertyName("content");
            encoder0.encode(writer, m.getContent());
        }
        if(m.getCreatedAt() != null){
            writer.setNextPropertyName("createdAt");
            encoder0.encode(writer, m.getCreatedAt());
        }
        if(m.getEndCondition() != null && m.getEndCondition().getValue() != null){
            writer.setNextPropertyName("endCondition");
            encoder0.encode(writer, m.getEndCondition());
        }
        if(m.getKey() != null){
            writer.setNextPropertyName("key");
            encoder0.encode(writer, m.getKey());
        }
        if(m.getMilestone() != null){
            writer.setNextPropertyName("milestone");
            encoder0.encode(writer, m.getMilestone());
        }
        if(m.getNo() != null){
            writer.setNextPropertyName("no");
            encoder0.encode(writer, m.getNo());
        }
        if(m.getParentTicketKey() != null){
            writer.setNextPropertyName("parentTicketKey");
            encoder0.encode(writer, m.getParentTicketKey());
        }
        if(m.getPeriod() != null){
            writer.setNextPropertyName("period");
            encoder0.encode(writer, m.getPeriod());
        }
        if(m.getPriority() != null){
            writer.setNextPropertyName("priority");
            encoder0.encode(writer, m.getPriority());
        }
        if(m.getProjectKey() != null){
            writer.setNextPropertyName("projectKey");
            encoder0.encode(writer, m.getProjectKey());
        }
        if(m.getStartDate() != null){
            writer.setNextPropertyName("startDate");
            encoder0.encode(writer, m.getStartDate());
        }
        if(m.getStatus() != null){
            writer.setNextPropertyName("status");
            encoder0.encode(writer, m.getStatus());
        }
        if(m.getTargetKind() != null){
            writer.setNextPropertyName("targetKind");
            encoder0.encode(writer, m.getTargetKind());
        }
        if(m.getTargetMemberKey() != null){
            writer.setNextPropertyName("targetMemberKey");
            encoder0.encode(writer, m.getTargetMemberKey());
        }
        if(m.getTargetVersion() != null){
            writer.setNextPropertyName("targetVersion");
            encoder0.encode(writer, m.getTargetVersion());
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
    protected jp.co.nemuzuka.model.TicketModel jsonToModel(org.slim3.datastore.json.JsonRootReader rootReader, int maxDepth, int currentDepth) {
        jp.co.nemuzuka.model.TicketModel m = new jp.co.nemuzuka.model.TicketModel();
        org.slim3.datastore.json.JsonReader reader = null;
        org.slim3.datastore.json.Default decoder0 = new org.slim3.datastore.json.Default();
        reader = rootReader.newObjectReader("category");
        m.setCategory(decoder0.decode(reader, m.getCategory()));
        reader = rootReader.newObjectReader("content");
        m.setContent(decoder0.decode(reader, m.getContent()));
        reader = rootReader.newObjectReader("createdAt");
        m.setCreatedAt(decoder0.decode(reader, m.getCreatedAt()));
        reader = rootReader.newObjectReader("endCondition");
        m.setEndCondition(decoder0.decode(reader, m.getEndCondition()));
        reader = rootReader.newObjectReader("key");
        m.setKey(decoder0.decode(reader, m.getKey()));
        reader = rootReader.newObjectReader("milestone");
        m.setMilestone(decoder0.decode(reader, m.getMilestone()));
        reader = rootReader.newObjectReader("no");
        m.setNo(decoder0.decode(reader, m.getNo()));
        reader = rootReader.newObjectReader("parentTicketKey");
        m.setParentTicketKey(decoder0.decode(reader, m.getParentTicketKey()));
        reader = rootReader.newObjectReader("period");
        m.setPeriod(decoder0.decode(reader, m.getPeriod()));
        reader = rootReader.newObjectReader("priority");
        m.setPriority(decoder0.decode(reader, m.getPriority()));
        reader = rootReader.newObjectReader("projectKey");
        m.setProjectKey(decoder0.decode(reader, m.getProjectKey()));
        reader = rootReader.newObjectReader("startDate");
        m.setStartDate(decoder0.decode(reader, m.getStartDate()));
        reader = rootReader.newObjectReader("status");
        m.setStatus(decoder0.decode(reader, m.getStatus()));
        reader = rootReader.newObjectReader("targetKind");
        m.setTargetKind(decoder0.decode(reader, m.getTargetKind()));
        reader = rootReader.newObjectReader("targetMemberKey");
        m.setTargetMemberKey(decoder0.decode(reader, m.getTargetMemberKey()));
        reader = rootReader.newObjectReader("targetVersion");
        m.setTargetVersion(decoder0.decode(reader, m.getTargetVersion()));
        reader = rootReader.newObjectReader("title");
        m.setTitle(decoder0.decode(reader, m.getTitle()));
        reader = rootReader.newObjectReader("version");
        m.setVersion(decoder0.decode(reader, m.getVersion()));
        return m;
    }
}