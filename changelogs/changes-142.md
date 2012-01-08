Version 1.4.2

==== **Bug Fixes:** ====

* party commands no longer error when Spout is not detected
* allowhatsplugin option is case sensetive but was reading as lowercase
* Only alert players of denied XP via block-break if they have the source of XP they are breaking the block of
* Removed startup warning for empty XP mappings
* we now cancel damage from fire sources if the player has the fire resist mob effect
* /hero admin prof no longer changes a players primary class to a secondary one.
* XP loss multipliers will now adjust past level loss properly, rather than being just based on the current level of the hero
* fix pistons being able to circumvent placed block logging

==== **General:** ====

* Added /hero admin heal <playername>
** heals a player to full health
** Requires heroes.admin.heal permission node
* Added a failure message to passive skills, if a hero tries to /skill <passiveskill>
* Secondary class costs are now seperate from primary - see new config.yml

==== **API:** ====

* QuickenEffect is now a common effect rather than being limited to SkillOne.

==== **Skills:** ====

* Charge
** usage text has been fixed
* Enchant
** fixed oddities when a player had Enchanting xp source, but not the skill
** SkillEnchant will also now block a player from opening the enchanting table if they don't have the skill
** Will now properly re-synch player inventory when an enchant is blocked
* Wolf
** Fixed usage text still indicating it was a usable skill rather than a passive