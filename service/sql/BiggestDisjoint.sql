-- Find attack with largest disjoint
SELECT C.fullName, animation,
   MAX(SQRT(POW(xoffset,2) + POW(yoffset,2) + POW(zoffset,2))) AS disjoint
FROM Hitboxes
   JOIN Characters C ON C.id = charId
GROUP BY charId, animation
ORDER BY MAX(SQRT(POW(xoffset,2) + POW(yoffset,2) + POW(zoffset,2))) DESC, C.fullName;
