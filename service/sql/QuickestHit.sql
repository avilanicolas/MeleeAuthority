-- Find quickest hitbox
SELECT fullName, SA.description, MIN(frame) AS earliestAttackFrame
FROM FrameStrips
   JOIN Characters C ON C.id = charId
   JOIN SharedAnimations SA.internalName = animation
WHERE hitbox = true
GROUP BY charId, animation
ORDER BY MIN(frame) ASC;
