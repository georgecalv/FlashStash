/**********************************************************************
 * NAME: George Calvert
 * CLASS: CPSC 321
 * DATE: 10/2/22
 * HOMEWORK: Problem Set 4
 * DESCRIPTION:  develop five “interesting” and “relevant” analytical queries for your final project
 **********************************************************************/
 
-- Question 1
SELECT COUNT(DISTINCT created_by) AS unique_sets
FROM User JOIN StudySet ON(created_by = username)
-- this is interesting since it is counting the number of sets created by unique users (no repeats)
-- meaning that it the number of sets created by individual users.

-- Question 2
SELECT username, COUNT(*) AS number_sets
FROM User JOIN StudySet ON(created_by = username)
GROUP BY username
-- this is interesting since it counts the number of studysets each user has created 
-- allowing me to sort users by those who have created the most sets in the whole database. 
-- seeing which users contribute the most to the community in terms of creating sets fro others to use.


-- Question 3
SELECT set_name, COUNT(*) AS number_saves
FROM StudySet JOIN Saves USING(set_id)
GROUP BY set_id
HAVING COUNT >= 2
ORDER BY number_saves DESC
-- this query is interesting since it finds the study sets with the most saves by users
-- and displays it in descending order. Allowing the user to filter sets and browse the ones with
-- the most saves above that of two other people

-- Question 4
SELECT s.set_name, s.created_by
FROM StudySet s
WHERE s.set_name IN (SELECT st.set_name FROM StudySet st WHERE st.set_id != s.set_id)
-- This is interesting since it returns the name and who created the study set of sets with the same name.
-- This would allow a user to distinguish different sets even if they have the same name or return all sets
-- with that name.

-- Question 5
SELECT username, COUNT(*) AS number_likes
FROM Likes
GROUP BY username
HAVING number_likes > (SELECT COUNT(*)
                          FROM Likes
                          GROUP BY username);
-- This query is intersting since it selects the usernames and the number of likes 
-- of the users who have liked the most studysets. IE the most active users allowing the program to promote sets
-- liked by these users.

