package cn.ksmcbrigade.killaura;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;
import net.legacyfabric.fabric.api.registry.CommandRegistry;
import net.minecraft.command.AbstractCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class KillauraMod implements ClientModInitializer {

	public static File config = new File("config/killaura-config.json");
	public static boolean enabled = false;

	public static boolean swing = true;
	public static double reach = 5D;
	public static int times = 2;

	@Override
	public void onInitializeClient() {
		new File("config").mkdirs();

		try {
			if(!config.exists()){
				save(false);
			}
			JsonObject object = new JsonParser().parse(FileUtils.readFileToString(config)).getAsJsonObject();
			swing = object.get("swing").getAsBoolean();
			reach = object.get("reach").getAsDouble();
			times = object.get("times").getAsInt();
		}
		catch (Exception e){
			e.printStackTrace();
		}

		CommandRegistry.INSTANCE.register(new AbstractCommand() {
			@Override
			public String getCommandName() {
				return "killaura-config";
			}

			@Override
			public String getUsageTranslationKey(CommandSource source) {
				return "/killaura-config (reach) (times) (swing) change or view the killAura mod config.";
			}

			@Override
			public void execute(CommandSource source, String[] args) throws CommandException {
				if(args.length>0){
					reach = Double.parseDouble(args[0]);
					if(args.length>=2){
						times = Integer.parseInt(args[1]);
						if(args.length>=3){
							swing = Boolean.parseBoolean(args[2]);
						}
					}
					source.sendMessage(new TranslatableText("gui.done"));
					try {
						save(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					source.sendMessage(new LiteralText("Swing: "+swing));
					source.sendMessage(new LiteralText("Reach: "+reach));
					source.sendMessage(new LiteralText("times: "+times));
				}
			}
		});

		System.out.println("Hello Fabric 1.8.9 world!");
	}

	public static void save(boolean ex) throws IOException {
		if(!config.exists() || ex){
			JsonObject object = new JsonObject();
			object.addProperty("reach",reach);
			object.addProperty("swing",swing);
			object.addProperty("times",times);
			FileUtils.writeStringToFile(config,object.toString());
		}
	}
}
