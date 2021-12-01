package cloud.lemonslice.silveroak.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig
{
    protected ServerConfig(ForgeConfigSpec.Builder builder)
    {
        Verification.load(builder);
    }

    public static class Verification
    {
        public static ForgeConfigSpec.ConfigValue<String> password;

        private static void load(ForgeConfigSpec.Builder builder)
        {
            builder.push("Verification");
            password = builder.comment("The password to try the alpha test.")
                    .define("Password", "0");
            builder.pop();
        }
    }
}

