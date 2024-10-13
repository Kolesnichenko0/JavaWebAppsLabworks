package csit.semit.kde.javahibernatewebappskdelab2.entity;

public interface SoftDeletable {
    void setIsDeleted(Boolean isDeleted);
    Boolean getIsDeleted();
}
