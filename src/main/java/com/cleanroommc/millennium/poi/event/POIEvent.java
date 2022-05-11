package com.cleanroommc.millennium.poi.event;

import com.cleanroommc.millennium.poi.PointOfInterest;
import net.minecraftforge.fml.common.eventhandler.Event;

public class POIEvent extends Event {
    private final PointOfInterest poi;

    public POIEvent(PointOfInterest poi) {
        this.poi = poi;
    }

    public static class AddedEvent extends POIEvent {
        public AddedEvent(PointOfInterest poi) {
            super(poi);
        }
    }
    public static class RemovedEvent extends POIEvent {
        public RemovedEvent(PointOfInterest poi) {
            super(poi);
        }
    }
    public static class UpdateEvent extends POIEvent {
        public UpdateEvent(PointOfInterest poi) {
            super(poi);
        }
    }
}
