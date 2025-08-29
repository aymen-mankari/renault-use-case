-- Clear tables before insertion


-- Insert into garage table
INSERT INTO garage (id, name, address, phone, email)
VALUES (1, 'Weekend Garage', '789 Pine Road', '+1-555-0789', 'weekend@garage.com');

-- Insert into schedule_time_slots table
INSERT INTO schedule_time_slots (schedule_garage_id, day_of_week, start_time, end_time)
VALUES (1, 'SATURDAY', '08:00:00', '18:00:00');

INSERT INTO schedule_time_slots (schedule_garage_id, day_of_week, start_time, end_time)
VALUES (1, 'SUNDAY', '09:00:00', '16:00:00');

INSERT INTO schedule_time_slots (schedule_garage_id, day_of_week, start_time, end_time)
VALUES (1, 'WEDNESDAY', '09:00:00', '16:00:00');

INSERT INTO vehicle (id, brand, manufacture_year, type_vehicle, fuel_type)
VALUES (1, 'Toyota', 2022, 'SEDANS', 'DIESEL');
INSERT INTO vehicle (id, brand, manufacture_year, type_vehicle, fuel_type)
VALUES (2, 'Renault', 2008, 'COUPE', 'DIESEL');
INSERT INTO vehicle (id, brand, manufacture_year, type_vehicle, fuel_type)
VALUES (3, 'Honda', 2008, 'COUPE', 'DIESEL');
INSERT INTO vehicle (id, brand, manufacture_year, type_vehicle, fuel_type)
VALUES (4, 'Buick', 1990, 'SUV', 'DIESEL');
INSERT INTO vehicle (id, brand, manufacture_year, type_vehicle, fuel_type)
VALUES (5, 'Dodge', 1990, 'COUPE', 'ESSENCE');
INSERT INTO vehicle (id, brand, manufacture_year, type_vehicle, fuel_type)
VALUES (6, 'Renault', 1990, 'COUPE', 'ESSENCE');


INSERT INTO garage_vehicle (garage_id, vehicle_id) VALUES (1, 1);
INSERT INTO garage_vehicle (garage_id, vehicle_id) VALUES (1, 2);
INSERT INTO garage_vehicle (garage_id, vehicle_id) VALUES (1, 3);
INSERT INTO garage_vehicle (garage_id, vehicle_id) VALUES (1, 4);
INSERT INTO garage_vehicle (garage_id, vehicle_id) VALUES (1, 6);



INSERT INTO accessory (id, name, description, price, type)
VALUES (1, 'Phone Holder', 'description accessory 1', 19.99, 'INTERIOR');
INSERT INTO accessory (id, name, description, price, type)
VALUES (2, 'Floor Mats', 'description accessory 2', 89.99, 'INTERIOR');
INSERT INTO accessory (id, name, description, price, type)
VALUES (3, 'Roof Rack', 'description accessory 3', 249.99, 'EXTERIOR');
INSERT INTO accessory (id, name, description, price, type)
VALUES (4, 'Dash Cam', 'description accessory 4', 159.99, 'ELECTRONICS');
INSERT INTO accessory (id, name, description, price, type)
VALUES (5, 'Dash Cam', 'description accessory 5', 300.99, 'ELECTRONICS');

INSERT INTO vehicle_accessory (vehicle_id, accessory_id) VALUES(1,1);
INSERT INTO vehicle_accessory (vehicle_id, accessory_id) VALUES(1,2);
INSERT INTO vehicle_accessory (vehicle_id, accessory_id) VALUES(2,1);
INSERT INTO vehicle_accessory (vehicle_id, accessory_id) VALUES(3,1);