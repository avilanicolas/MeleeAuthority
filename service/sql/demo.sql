-- marth fair query
SELECT
    CAC.commandIndex,
    ACT.name,
    HEX(CAC.commandData)
FROM
    SharedAnimations SA
    JOIN CharacterAnimationCommands CAC ON CAC.animation = SA.internalName
    JOIN AnimationCommandTypes ACT ON CAC.commandType = ACT.id
WHERE
    CAC.charId = 'Ms'
    AND SA.description = 'Forward-Air'
ORDER BY CAC.commandIndex;
