-- Clear tables before insertion
DELETE FROM schedule_time_slots;
DELETE FROM garage;

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