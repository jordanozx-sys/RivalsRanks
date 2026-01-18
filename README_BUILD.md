# RivalsRanks (auto-build jar)

## Get a ready-to-download .jar without installing Java/Maven
1. Create a GitHub repo (empty).
2. Upload ALL files from this zip into the repo (drag-and-drop in GitHub web UI).
3. Go to **Actions** tab → enable workflows if asked.
4. Click the workflow run (or go to Actions → build → Run workflow).
5. Download the **rivals-ranks-jar** artifact.
6. Put the jar into your server `/plugins` folder and restart.

## Commands
- /rank get [player]
- /rank set <player> <OWNER|ADMIN|MOD|MEMBER>
- /rank list
- /rank reload
