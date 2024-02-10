-- Запит для видалення даних з таблиці accommodation
DELETE FROM accommodations_amenities;

-- Запит для видалення даних з таблиці amenties
DELETE FROM bookings;
DELETE FROM accommodations;

-- Запит для видалення даних з таблиць users_roles та users
-- Будьте обережні! Видалення даних з таблиці users_roles може призвести до втрати зв'язку між користувачами та їх ролями.
-- У реальному застосунку вам може знадобитися реалізувати більш обережну логіку видалення, яка зберігає консистентність даних.
DELETE FROM users_roles;
DELETE FROM users;
