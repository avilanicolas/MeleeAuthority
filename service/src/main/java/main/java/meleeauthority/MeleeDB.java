package main.java.meleeauthority;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.ImmutableList;

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

    public List<String> getCharacterIdentifiers() {
        List<Map<String, Object>> result = template.queryForList("SELECT id FROM Characters");
        ImmutableList.Builder<String> builder = new ImmutableList.Builder<String>();

        for (Map<String, Object> entry : result) {
            builder.add((String) entry.get("id"));
        }

        return builder.build();
    }

    public List<MeleeCharacter> getAllCharacters() {
        List<Map<String, Object>> result = template.queryForList(
            "SELECT id FROM Characters C" +
            "JOIN CharacterAttributes CA ON CA.id = C.id");

        ImmutableList.Builder<MeleeCharacter> characterList = new ImmutableList.Builder<MeleeCharacter>();
        for (Map<String, Object> entry : result) {
            MeleeCharacter newCharacter = new MeleeCharacter();
            newCharacter.AirFriction = Float.parseFloat((String) entry.get("AirFriction"));
            newCharacter.AirJMult = Float.parseFloat((String) entry.get("AirJMult"));
            newCharacter.AirMobA = Float.parseFloat((String) entry.get("AirMobA"));
            newCharacter.AirMobB = Float.parseFloat((String) entry.get("AirMobB"));
            newCharacter.ALag = Integer.parseInt((String) entry.get("ALag"));
            newCharacter.BLag = Integer.parseInt((String) entry.get("BLag"));
            newCharacter.ChDirFrames = Integer.parseInt((String) entry.get("ChDrFrames"));
            newCharacter.DashAccelA = Float.parseFloat((String) entry.get("DashAccelA"));
            newCharacter.DashAccelB = Float.parseFloat((String) entry.get("DashAccelB"));
            newCharacter.DashInitVel = Float.parseFloat((String) entry.get("DashInitVel"));
            newCharacter.DashTermVel = Float.parseFloat((String) entry.get("DashTermVel"));
            newCharacter.DblJMult = Float.parseFloat((String) entry.get("DashTermVel"));
            newCharacter.DLag = Integer.parseInt((String) entry.get("DLag"));
            newCharacter.FastWalkMin = Float.parseFloat((String) entry.get("FastWalkMin"));
            newCharacter.FFTermVel = Float.parseFloat((String) entry.get("FFTermVel"));
            newCharacter.FLag = Integer.parseInt((String) entry.get("FLag"));
            newCharacter.Friction = Float.parseFloat((String) entry.get("Fricton"));
            newCharacter.Gravity = Float.parseFloat((String) entry.get("Gravity"));
            newCharacter.InitWalkVel = Float.parseFloat((String) entry.get("InitWalkVel"));
            newCharacter.Jab2Window = Integer.parseInt((String) entry.get("Jab2Window"));
            newCharacter.Jab3Window = Integer.parseInt((String) entry.get("Jab3Window"));
            newCharacter.JumpFrames = Float.parseFloat((String) entry.get("JumpFrames"));
            newCharacter.JumpHInitVel = Float.parseFloat((String) entry.get("JumpHInitVel"));
            newCharacter.JumpHMaxVel = Float.parseFloat((String) entry.get("JumpHMaxVel"));
            newCharacter.JumpMomentMult = Float.parseFloat((String) entry.get("JumpMomentMult"));
            newCharacter.JumpVInitVel = Float.parseFloat((String) entry.get("JumpVInitVel"));
            newCharacter.LdgJmpHVel = Float.parseFloat((String) entry.get("LdgJumpHVel"));
            newCharacter.LdgJmpVVel = Float.parseFloat((String) entry.get("LdgJumpVVel"));
            newCharacter.MaxAirHVel = Float.parseFloat((String) entry.get("MaxAirHVel"));
            newCharacter.MidWalkPoint = Float.parseFloat((String) entry.get("MidWalkPoint"));
            newCharacter.ModelScaling = Float.parseFloat((String) entry.get("ModelScaling"));
            newCharacter.NLag = Integer.parseInt((String) entry.get("NLag"));
            newCharacter.NumJumps = Integer.parseInt((String) entry.get("NumJumps"));
            newCharacter.RpdJabWindow = Integer.parseInt((String) entry.get("RpdJabWindow"));
            newCharacter.RunAccel = Float.parseFloat((String) entry.get("RunAccel"));
            newCharacter.RunAnimScal = Float.parseFloat((String) entry.get("RunAnimScal"));
            newCharacter.ShieldSize = Float.parseFloat((String) entry.get("ShieldSize"));
            newCharacter.ShldBrkInitVel = Float.parseFloat((String) entry.get("ShldBrkInitVel"));
            newCharacter.SHVInitVel = Float.parseFloat((String) entry.get("SHVInitVel"));
            newCharacter.SlowWalkMax = Float.parseFloat((String) entry.get("SlowWalkMax"));
            newCharacter.StarDmg = Integer.parseInt((String) entry.get("StarDmg"));
            newCharacter.TermVel = Float.parseFloat((String) entry.get("TermVel"));
            newCharacter.ThrowVel = Float.parseFloat((String) entry.get("ThrowVel"));
            newCharacter.ULag = Integer.parseInt((String) entry.get("ULag"));
            newCharacter.VMdlScaling = Integer.parseInt((String) entry.get("VMdlScaling"));
            newCharacter.WalkAccel = Float.parseFloat((String) entry.get("WalkAccel"));
            newCharacter.WalkMaxVel = Float.parseFloat((String) entry.get("WalkMaxVel"));
            newCharacter.Weight = Integer.parseInt((String) entry.get("Weight"));
            newCharacter.WJmpHVel = Float.parseFloat((String) entry.get("ThrowVel"));
            newCharacter.WJmpVVel = Float.parseFloat((String) entry.get("ThrowVel"));
            characterList.add(newCharacter);
        }

        return characterList.build();
    }
}
