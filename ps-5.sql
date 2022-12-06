-- /**********************************************************************
--  * NAME: George Calvert
--  * CLASS: CPSC 321
--  * DATE: 10/2/22
--  * HOMEWORK: Problem Set 4
--  * DESCRIPTION: develop five “interesting” and “relevant” analytical queries for your final project
--  **********************************************************************/

-- -- Question 1
-- SELECT set_id, created_by, username AS saved_by
-- FROM Saves RIGHT OUTER JOIN StudySet USING(set_id);
-- -- This query is interesting since it returns all sets in the database but leaves a NULL value 
-- -- in created by if a set is not saved by another user

-- -- Question 2
-- (SELECT set_id, "SET" AS type FROM StudySet)
-- UNION
-- (SELECT set_id, "SAVED" AS type FROM Saves);
-- -- This query is interesting since it selects all set IDS and compares them to ones that were saved.
-- -- Meaning that you can easily see the number of sets that were saved compared to not saved.
-- -- Question 3
-- WITH saves AS(
--     SELECT set_id, set_name, created_by, username AS saved_by
--     FROM Saves RIGHT OUTER JOIN StudySet USING(set_id)  
-- )
-- SELECT set_id, created_by, 
--     CASE
--         WHEN saved_by IS NULL THEN "saved"
--         ELSE "not saved"
--     END AS saved
-- FROM saves
-- GROUP BY set_id;
-- -- This query is interesting since it changes the null values to more readable values like saved or not saved
-- -- if a study set tat is created by a user is svaed by another user

-- Question 4
WITH number_created AS(
    SELECT username, COUNT(*) AS number_sets
    FROM User JOIN StudySet ON(created_by = username)
    GROUP BY username
    ORDER BY number_sets DESC
)
SELECT username, number_sets, DENSE_RANK() OVER (ORDER BY number_sets DESC) AS rank
FROM number_created;
-- This query is interesting since it ranks the users based on the number of study sets they have created/
-- For instance rank 1 has the most sets and then descending down

-- -- Question 5
-- WITH most_saved_sets AS(
--     SELECT set_name, COUNT(*) AS number_saves
--     FROM StudySet JOIN Saves USING(set_id)
--     GROUP BY set_id
--     HAVING number_saves >= 1
--     ORDER BY number_saves DESC
-- )
-- SELECT set_name, number_saves, DENSE_RANK() OVER (ORDER BY number_saves DESC) AS rank
-- FROM most_saved_sets;
-- -- This query is interesting since it ranks the sets based on the number of saves each one has.
-- -- For example sets with the most saves would have rank 1. This allows me to rank the sets for users
-- -- for the most used sets.

