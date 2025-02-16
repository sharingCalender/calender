package sharingcalender.calender.repository.qdsl;

public interface UserGroupQueryDSLRepository {

    void deleteByCalendarGroupId(long calendarGroupId);

    void deleteUserGroupByMember(long calendarGroupId, String username);

    boolean userIsExist(long calendarGroupId, String username);
}
