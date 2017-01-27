package fun.mota.com.barscanner.event;

/**
 * Created by shaileshmota on 26/01/2017.
 * Abstract class for any type of event.
 */

public abstract class Event {
    private Enum type;
    Event(Enum type) {
        this.type = type;
    }

    public Enum getType() {
        return type;
    }

    public void setType(Enum type) {
        this.type = type;
    }


}
