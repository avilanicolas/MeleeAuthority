-- Find quickest hitbox
SELECT fullName, animation, MIN(frame) AS earliestAttackFrame
FROM FrameStrips
   JOIN Characters C ON C.id = charId
WHERE hitbox = true
GROUP BY charId, animation
ORDER BY MIN(frame) ASC;
