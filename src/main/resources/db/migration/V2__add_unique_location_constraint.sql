ALTER TABLE locations
ADD CONSTRAINT unique_location_for_user
UNIQUE (user_id, name, latitude, longitude);