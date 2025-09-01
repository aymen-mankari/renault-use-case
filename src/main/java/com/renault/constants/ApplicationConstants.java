package com.renault.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstants {
    public static final String DATA_NOT_FOUND_EXCEPTION_MESSAGE_GARAGE = "Garage not found with ID : %s";
    public static final String DATA_NOT_FOUND_EXCEPTION_MESSAGE_VEHICLE = "Vehicle not found with ID : %s";
    public static final String DATA_NOT_FOUND_EXCEPTION_MESSAGE_ACCESSORY = "Accessory not found with ID : %s";
    public static final String BAD_REQUEST_EXCEPTION_GARAGE_SIZE_REACHED = "Cannot add vehicle to the garage, max size reached";

    public static final String BAD_REQUEST_EXCEPTION_VEHICLE_NOT_FOUND_IN_GARAGE = "Vehicle [%s] doesn't belong to this garage [%s]";
    public static final String BAD_REQUEST_EXCEPTION_ACCESSORY_NOT_FOUND_IN_ACCESSORY_LIST = "Accessory [%s] not found in the vehicle accessories list";
    public static final String BAD_REQUEST_EXCEPTION_ACCESSORY_ALREADY_FOUND_IN_ACCESSORY_LIST = "Accessory [%s] already found in the vehicle accessories list";
    public static final String BAD_REQUEST_EXCEPTION_ACCESSORY_ALREADY_EXIST_IN_GARAGE = "Vehicle [%s] already exist in the garage [%s]";



}
