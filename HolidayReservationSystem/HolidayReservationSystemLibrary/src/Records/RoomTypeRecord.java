
package Records;

import java.io.Serializable;

public class RoomTypeRecord implements Serializable{
    
    private String roomTypeName;

    public RoomTypeRecord() {
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }
    
}
