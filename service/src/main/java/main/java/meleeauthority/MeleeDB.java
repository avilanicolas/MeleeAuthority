package main.java.meleeauthority;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class MeleeDB implements MeleeDAO {
    private DataSource dataSource;
    private JdbcTemplate template;

    @Override
    public void setDataSource(DataSource ds) {
        this.dataSource = ds;
        this.template = new JdbcTemplate(ds);
    }

    @Override
    public void asdf() {
        System.out.println(template.queryForList("SHOW TABLES").size());
    }

    public List<String> getCharacterIds() {
        List<Map<String, Object>> result = template.queryForList("SELECT charId FROM Characters");
        ImmutableList.Builder<String> builder = new ImmutableList.Builder<String>();

        for (Map<String, Object> entry : result) {
            builder.add((String) entry.get("charId"));
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

    

    public List<MeleeMove> getAllMoves() {
        ImmutableList.Builder<MeleeMove> moveList = new ImmutableList.Builder<MeleeMove>();

        List<Map<String, Object>> rawMoveList = template.queryForList(
            "SELECT DISTINCT charId, animation FROM CharacterAnimationCommands");

        for (Map<String, Object> rawMove : rawMoveList) {
            String commandQuery = String.format(
                "SELECT * FROM Characters C " +
                "JOIN CharacterAnimationCommands CAC ON CAC.charId = C.id " +
                "JOIN AnimationCommantTypes ACT ON ACT.id = CAC.commandType " +
                "JOIN SharedAnimations SA ON SA.internalName = animation " +
                "WHERE CAC.charId = '%s' AND CAC.animation = '%s' " +
                "ORDER BY CAC.commandIndex ASC",
                rawMove.get("charId"),
                rawMove.get("animation"));

            MeleeMove move = new MeleeMove();
            move.charId = (String) rawMove.get("charId");
            List<Map<String, Object>> rawCommandList = template.queryForList(commandQuery);
            ImmutableList.Builder<AnimationCommand> commandList = new ImmutableList.Builder<AnimationCommand>();
            for (Map<String, Object> rawCommand : rawCommandList) {
                AnimationCommand command = new AnimationCommand();

                command.data = (Integer) rawCommand.get("commandData");
                command.type = (String) rawCommand.get("commandType");

                commandList.add(command);
            }

            move.commands = commandList.build();
            moveList.add(move);
        }

        return moveList.build();
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
        newCharacter.ALag = (Integer) entry.get("ALag");
        newCharacter.BLag = (Integer) entry.get("BLag");
        newCharacter.ChDirFrames = (Integer) entry.get("ChDrFrames");
        newCharacter.DashAccelA = (Float) entry.get("DashAccelA");
        newCharacter.DashAccelB = (Float) entry.get("DashAccelB");
        newCharacter.DashInitVel = (Float) entry.get("DashInitVel");
        newCharacter.DashTermVel = (Float) entry.get("DashTermVel");
        newCharacter.DblJMult = (Float) entry.get("DashTermVel");
        newCharacter.DLag = (Integer) entry.get("DLag");
        newCharacter.FastWalkMin = (Float) entry.get("FastWalkMin");
        newCharacter.FFTermVel = (Float) entry.get("FFTermVel");
        newCharacter.FLag = (Integer) entry.get("FLag");
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
        newCharacter.NLag = (Integer) entry.get("NLag");
        newCharacter.NumJumps = (Integer) entry.get("NumJumps");
        newCharacter.RpdJabWindow = (Integer) entry.get("RpdJabWindow");
        newCharacter.RunAccel = (Float) entry.get("RunAccel");
        newCharacter.RunAnimScal = (Float) entry.get("RunAnimScal");
        newCharacter.ShieldSize = (Float) entry.get("ShieldSize");
        newCharacter.ShldBrkInitVel = (Float) entry.get("ShldBrkInitVel");
        newCharacter.SHVInitVel = (Float) entry.get("SHVInitVel");
        newCharacter.SlowWalkMax = (Float) entry.get("SlowWalkMax");
        newCharacter.StarDmg = (Integer) entry.get("StarDmg");
        newCharacter.TermVel = (Float) entry.get("TermVel");
        newCharacter.ThrowVel = (Float) entry.get("ThrowVel");
        newCharacter.ULag = (Integer) entry.get("ULag");
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
