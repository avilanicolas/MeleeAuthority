package main.java.meleeauthority;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class MeleeDB implements MeleeDAO {
    private DataSource dataSource;
    private JdbcTemplate template;

    // This is a really long latency call, so wrap it in an optional to save us some sanity.
    private Optional<List<MeleeMove>> allMeleeMoves = Optional.absent();

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.template = new JdbcTemplate(ds);
    }

    @Override
    public void asdf() {
        System.out.println(template.queryForList("SHOW TABLES").size());
    }

    public Map<String, List<Hitbox>> getHitboxesForCharacter(String charId) {
        String sql = String.format(
            "SELECT * FROM Hitboxes " +
            "WHERE charId = '%s'",
            charId,
            animation);

        new ImmutableMap.Builder<String, List<Hitbox>> hitboxMap =
            new ImmutableMap.Builder<String, List<Hitbox>>();

        List<Map<String, Object>> result = template.queryForList(sql);
        for (Map<String, Object> entry : result) {
            hitboxMap.put(entry.get("animation"), getHitboxesForMove(charId, entry.get("animation")));
        }

        return hitboxMap.build();
    }

    public List<Hitbox> getHitboxesForMove(String charId, String animation) {
        String sql = String.format(
            "SELECT * FROM Hitboxes " +
            "WHERE charId = '%s' AND animation = '%s'",
            charId,
            animation);

        ImmutableList.Builder<Hitbox> hitboxList =
            new ImmutableList.Builder<Hitbox>();
        List<Map<String, Object>> result =
            template.queryForList(sql);

        for (Map<String, Object> entry : result) {
            Hitbox hitbox = new Hitbox();

            hitbox.groupId = (Integer) entry.get("groupId");
            hitbox.hitboxId = (Integer) entry.get("hitboxId");
            hitbox.bone = (Integer) entry.get("bone");
            hitbox.damage = (Integer) entry.get("damage");
            hitbox.zoffset = (Integer) entry.get("zoffset");
            hitbox.yoffset = (Integer) entry.get("yoffset");
            hitbox.xoffset = (Integer) entry.get("xoffset");
            hitbox.angle = (Integer) entry.get("angle");
            hitbox.knockbackScaling = (Integer) entry.get("knockbackScaling");
            hitbox.fixedKnockback = (Integer) entry.get("fixedKnockback");
            hitbox.baseKnockback = (Integer) entry.get("baseKnockback");
            hitbox.shieldDamage = (Integer) entry.get("shieldDamage");

            hitboxList.add(hitbox);
        }

        return hitboxList.build();
    }

    public List<String> getCharacterIds() {
        List<Map<String, Object>> result = template.queryForList("SELECT id FROM Characters");
        ImmutableList.Builder<String> builder = new ImmutableList.Builder<String>();

        for (Map<String, Object> entry : result) {
            builder.add((String) entry.get("id"));
        }

        return builder.build();
    }

    public List<String> getCharacterNames() {
        List<Map<String, Object>> result = template.queryForList("SELECT fullName FROM Characters");
        ImmutableList.Builder<String> builder = new ImmutableList.Builder<String>();

        for (Map<String, Object> entry : result) {
            builder.add((String) entry.get("fullName"));
        }

        return builder.build();
    }

    public List<MeleeMove> getMovesForCharacter(String charId) {
        if (!allMeleeMoves.isPresent()) {
            getAllMoves();
        }

        ImmutableList.Builder<MeleeMove> moveList = new ImmutableList.Builder<MeleeMove>();

        for (MeleeMove move : allMeleeMoves.get()) {
            if (move.charId.equals(charId)) {
                moveList.add(move);
            }
        }

        return moveList.build();
    }

    public List<String> getCharacterAnimations() {
        ImmutableList.Builder<String> animationList = new ImmutableList.Builder<String>();
 
        List<Map<String, Object>> rawAnimationList = template.queryForList(
            "SELECT internalName FROM SharedAnimations");

        for (Map<String, Object> animation : rawAnimationList) {
            animationList.add((String) animation.get("internalName"));
        }

        return animationList.build();
    }

    public List<MeleeMove> getMovesQualifiedBy(String charId, String animation) {
        if (animation == null) {
            return ImmutableList.of();
        }

        if (!allMeleeMoves.isPresent()) {
            getAllMoves();
        }

        ImmutableList.Builder<MeleeMove> moveList = new ImmutableList.Builder<MeleeMove>();

        for (MeleeMove move : allMeleeMoves.get()) {
            if (charId == null && move.internalName.equals(animation) ||
                move.charId.equals(charId) && move.internalName.equals(animation)) {
                moveList.add(move);
            }
        }

        return moveList.build();
    }

    public List<MeleeMove> getAllMoves() {
        if (allMeleeMoves.isPresent()) {
            return allMeleeMoves.get();
        }

        ImmutableList.Builder<MeleeMove> moveList = new ImmutableList.Builder<MeleeMove>();

        List<Map<String, Object>> rawMoveList = template.queryForList(
            "SELECT DISTINCT charId, animation FROM CharacterAnimationCommands");

        for (Map<String, Object> rawMove : rawMoveList) {
            moveList.add(buildMeleeMove(
                (String) rawMove.get("charId"),
                (String) rawMove.get("animation")));
        }

        allMeleeMoves = Optional.of(moveList.build());
        return allMeleeMoves.get();
    }

    private MeleeMove buildMeleeMove(String charId, String animation) {
        String commandQuery = String.format(
            "SELECT * FROM Characters C " +
            "JOIN CharacterAnimationCommands CAC ON CAC.charId = C.id " +
            "JOIN AnimationCommandTypes ACT ON ACT.id = CAC.commandType " +
            "JOIN SharedAnimations SA ON SA.internalName = animation " +
            "WHERE CAC.charId = '%s' AND CAC.animation = '%s' " +
            "ORDER BY CAC.commandIndex ASC",
            charId,
            animation);

        MeleeMove move = new MeleeMove();
        move.charId = charId;

        List<Map<String, Object>> rawCommandList = template.queryForList(commandQuery);
        move.animation = (String) rawCommandList.get(0).get("description");
        move.internalName = (String) rawCommandList.get(0).get("internalName"); 

        ImmutableList.Builder<AnimationCommand> commandList = new ImmutableList.Builder<AnimationCommand>();
        for (Map<String, Object> rawCommand : rawCommandList) {
            AnimationCommand command = new AnimationCommand();

            command.type = (String) rawCommand.get("name");

            StringBuilder dataBuilder = new StringBuilder();
            byte[] data = (byte[]) rawCommand.get("commandData");
            for (byte b : data) {
                dataBuilder.append(String.format("%02X", b));
            }
            command.data = dataBuilder.toString();

            commandList.add(command);
        }

        String frameQuery = String.format(
            "SELECT * FROM FrameStrips WHERE charId = '%s' AND animation = '%s' ORDER BY frame ASC",
            charId,
            move.internalName);
        List<Map<String, Object>> rawFrameList = template.queryForList(frameQuery);

        ImmutableList.Builder<AnimationFrame> frameList = new ImmutableList.Builder<AnimationFrame>();
        for (Map<String, Object> rawFrame : rawFrameList) {
            AnimationFrame frame = new AnimationFrame();

            frame.autocancel = (boolean) rawFrame.get("autocancel");
            frame.iasa = (boolean) rawFrame.get("iasa");
            frame.hitbox = (boolean) rawFrame.get("hitbox");

            frameList.add(frame);
        }

        move.frames = frameList.build();
        move.commands = commandList.build();
        return move;
    }

    public Set<String> getFilterableCharacterFields() {
        ImmutableSet.Builder<String> filterableSet = new ImmutableSet.Builder<String>();

        List<Map<String, Object>> result = template.queryForList(
            "SELECT column_name FROM information_schema.columns " +
            "WHERE table_name='CharacterAttributes' AND column_type != 'char(2)'");
    
        for (Map<String, Object> entry : result) {
            filterableSet.add((String) entry.get("column_name"));
        }

        return filterableSet.build();
    }

    public MeleeCharacter getCharacterById(String charId) {
        List<Map<String, Object>> result = template.queryForList(String.format(
            "SELECT * FROM Characters C " +
            "JOIN CharacterAttributes CA ON CA.id = C.id " +
            "WHERE C.id = '%s'", charId));

        return translateEntry(result.get(0));
    }

    public MeleeCharacter getCharacter(String name) {
        List<Map<String, Object>> result = template.queryForList(String.format(
            "SELECT * FROM Characters C " +
            "JOIN CharacterAttributes CA ON CA.id = C.id " +
            "WHERE fullName = '%s'", name));

        return translateEntry(result.get(0));
    }

    public List<MeleeCharacter> getFilteredCharacters(
        String filterName,
        CharacterPredicate predicate,
        Float value) {
        ImmutableList.Builder<MeleeCharacter> characterSet = new ImmutableList.Builder<MeleeCharacter>();

        if (value != null) {
            List<Map<String, Object>> result = template.queryForList(String.format(
                "SELECT * FROM Characters C " +
                "JOIN CharacterAttributes CA ON CA.id = C.id " +
                "WHERE %s %s %f",
                filterName,
                predicate.toString(),
                value));

            for (Map<String, Object> entry : result) {
                characterSet.add(translateEntry(entry));
            }
        } else {
            System.out.printf("REJECTED %s %s %s (value == null)\n", filterName, predicate, value);
        }

        return characterSet.build();
    }

    public List<MeleeCharacter> getAllCharacters() {
        List<Map<String, Object>> result = template.queryForList(
            "SELECT * FROM Characters C " +
            "JOIN CharacterAttributes CA ON CA.id = C.id");

        ImmutableList.Builder<MeleeCharacter> characterList = new ImmutableList.Builder<MeleeCharacter>();
        for (Map<String, Object> entry : result) {
            characterList.add(translateEntry(entry));
        }

        return characterList.build();
    }

    private MeleeCharacter translateEntry(Map<String, Object> entry) {
        MeleeCharacter newCharacter = new MeleeCharacter();

        newCharacter.AirFriction = (Float) entry.get("AirFriction");
        newCharacter.AirJMult = (Float) entry.get("AirJMult");
        newCharacter.AirMobA = (Float) entry.get("AirMobA");
        newCharacter.AirMobB = (Float) entry.get("AirMobB");
        newCharacter.ALag = (Float) entry.get("ALag");
        newCharacter.BLag = (Float) entry.get("BLag");
        newCharacter.ChDirFrames = (Integer) entry.get("ChDrFrames");
        newCharacter.DashAccelA = (Float) entry.get("DashAccelA");
        newCharacter.DashAccelB = (Float) entry.get("DashAccelB");
        newCharacter.DashInitVel = (Float) entry.get("DashInitVel");
        newCharacter.DashTermVel = (Float) entry.get("DashTermVel");
        newCharacter.DblJMult = (Float) entry.get("DashTermVel");
        newCharacter.DLag = (Float) entry.get("DLag");
        newCharacter.FastWalkMin = (Float) entry.get("FastWalkMin");
        newCharacter.FFTermVel = (Float) entry.get("FFTermVel");
        newCharacter.FLag = (Float) entry.get("FLag");
        newCharacter.Friction = (Float) entry.get("Fricton");
        newCharacter.Gravity = (Float) entry.get("Gravity");
        newCharacter.InitWalkVel = (Float) entry.get("InitWalkVel");
        newCharacter.Jab2Window = (Integer) entry.get("Jab2Window");
        newCharacter.Jab3Window = (Integer) entry.get("Jab3Window");
        newCharacter.JumpFrames = (Float) entry.get("JumpFrames");
        newCharacter.JumpHInitVel = (Float) entry.get("JumpHInitVel");
        newCharacter.JumpHMaxVel = (Float) entry.get("JumpHMaxVel");
        newCharacter.JumpMomentMult = (Float) entry.get("JumpMomentMult");
        newCharacter.JumpVInitVel = (Float) entry.get("JumpVInitVel");
        newCharacter.LdgJmpHVel = (Float) entry.get("LdgJumpHVel");
        newCharacter.LdgJmpVVel = (Float) entry.get("LdgJumpVVel");
        newCharacter.MaxAirHVel = (Float) entry.get("MaxAirHVel");
        newCharacter.MidWalkPoint = (Float) entry.get("MidWalkPoint");
        newCharacter.ModelScaling = (Float) entry.get("ModelScaling");
        newCharacter.NLag = (Float) entry.get("NLag");
        newCharacter.NumJumps = (Integer) entry.get("NumJumps");
        newCharacter.RpdJabWindow = (Integer) entry.get("RpdJabWindow");
        newCharacter.RunAccel = (Float) entry.get("RunAccel");
        newCharacter.RunAnimScal = (Float) entry.get("RunAnimScal");
        newCharacter.ShieldSize = (Float) entry.get("ShieldSize");
        newCharacter.ShldBrkInitVel = (Float) entry.get("ShldBrkInitVel");
        newCharacter.SHVInitVel = (Float) entry.get("SHVInitVel");
        newCharacter.SlowWalkMax = (Float) entry.get("SlowWalkMax");
        newCharacter.StarDmg = (Float) entry.get("StarDmg");
        newCharacter.TermVel = (Float) entry.get("TermVel");
        newCharacter.ThrowVel = (Float) entry.get("ThrowVel");
        newCharacter.ULag = (Float) entry.get("ULag");
        newCharacter.VMdlScaling = (Float) entry.get("VMdlScaling");
        newCharacter.WalkAccel = (Float) entry.get("WalkAccel");
        newCharacter.WalkMaxVel = (Float) entry.get("WalkMaxVel");
        newCharacter.Weight = (Integer) entry.get("Weight");
        newCharacter.WJmpHVel = (Float) entry.get("WJmpHVel");
        newCharacter.WJmpVVel = (Float) entry.get("WJmpVVell");
        newCharacter.id = (String) entry.get("id");
        newCharacter.name = (String) entry.get("fullName");

        return newCharacter;
    }

}
