package com.renault.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstants {
    public static final String DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE = "Garage not found with ID :";
    public static final String DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE = "Vehicle not found with ID :";
    public static final String DATA_NOT_FOUND_EXCEPTION_MESSAGE_ACCESSORY = "Accessory not found with ID :";
    public static final String BAD_REQUEST_EXCEPTION_GARAGE_SIZE_REACHED = "Cannot add vehicle to the garage, max size reached";

    public static final String BAD_REQUEST_EXCEPTION_VEHICLE_NOT_FOUND_IN_GARAGE = "Vehicle [%s] doesn't belong to this garage [%s]";


}
