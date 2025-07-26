package sharingcalender.calender.dto.calendar.response;


import java.util.List;

public record EventListResponseDto (
    List<EventInfoResponseDto> eventList
){

    public static EventListResponseDto create(List<EventInfoResponseDto> eventList){
        return new EventListResponseDto(eventList);
    }
}
