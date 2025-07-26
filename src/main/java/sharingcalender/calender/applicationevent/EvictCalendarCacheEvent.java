package sharingcalender.calender.applicationevent;

public record EvictCalendarCacheEvent (
    long calendarGroupId
){

    public static EvictCalendarCacheEvent create(long calendarGroupId){
        return new EvictCalendarCacheEvent(calendarGroupId);
    }
}
