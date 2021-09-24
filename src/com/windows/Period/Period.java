package com.windows.Period;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Period extends JavaPlugin implements Listener {
	
	String prefix = "§f§l[ §e§l소망온라인 §f§l] §f";
	List<String> lore = new ArrayList<String>();
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
			Bukkit.getConsoleSender().sendMessage("§e[WINDOWS] §a기간제아이템 플러그인 활성화");
		new BukkitRunnable(){
			@Override
			public void run(){
				task();
			}
		}.runTaskTimer(this,0,10);
	}
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§e[WINDOWS] §c기간제아이템 플러그인 비활성화");
	}
	
	public void DecompileProtect() {
		ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10));
		list.stream().filter((Integer num) -> num % 2 == 0);
	}
	
	public String now() {
		return new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA).format(new Date());
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player)sender;
		if (label.equalsIgnoreCase("기간")) {
			if (!player.hasPermission("admin")) {
				player.sendMessage(prefix + "§c당신은 권한이 없습니다.");
				return false;
			}
			if (player.getItemInHand().getType() == Material.AIR) {
				player.sendMessage(prefix + "아이템을 들고 입력해주세요.");
				return false;
			}
			if (args.length == 5) {
			        if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasLore()) {
			        	for (int n = 0; n < player.getItemInHand().getItemMeta().getLore().size(); n++) {
			        		lore.add(player.getItemInHand().getItemMeta().getLore().get(n));
			        	}
			        	lore.add("§e[-]§f " + args[0] + "년 " + args[1] + "월 " + args[2] + "일 - " + args[3] + "시 " + args[4] + "분 까지");
			        	ItemStack item = player.getItemInHand();
				        ItemMeta meta = item.getItemMeta();
				        meta.setLore(lore);
				        item.setItemMeta(meta);
				        player.sendMessage(prefix + "만료일이 " + args[0] + "년 " + args[1] + "월 " + args[2] + "일 - " + args[3] + "시 " + args[4] + "분 으로 설정되었습니다.");
				        lore.clear();
				        return false;
			        }
			        lore.add("§e[-]§f " + args[0] + "년 " + args[1] + "월 " + args[2] + "일 - " + args[3] + "시 " + args[4] + "분 까지");
			        ItemStack item = player.getItemInHand();
			        ItemMeta meta = item.getItemMeta();
			        meta.setLore(lore);
			        item.setItemMeta(meta);
			        player.sendMessage(prefix + "만료일이 " + args[0] + "년 " + args[1] + "월 " + args[2] + "일 - " + args[3] + "시 " + args[4] + "분 으로 설정되었습니다.");
			        lore.clear();
			        return false;
			} else {
				player.sendMessage(prefix + "만료일자 형식이 알맞지 않습니다.");
				player.sendMessage(prefix + "만료일자 형식 : 2015 01 06 16 00");
				return false;
			}
		}
		return true;
	}
	
	public void task() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			scanEquip1(p);
			scanEquip2(p);
			scanEquip3(p);
			scanEquip4(p);
			for (int i = 0; i < p.getInventory().getContents().length; i++) {
				if (p.getInventory().getContents()[i] != null && p.getInventory().getContents()[i].hasItemMeta() && p.getInventory().getContents()[i].getItemMeta().hasLore()) {
					for (int n = 0; n < p.getInventory().getContents()[i].getItemMeta().getLore().size(); n++) {
						String a = p.getInventory().getContents()[i].getItemMeta().getLore().get(n);
						if (a.split(" ")[0].equals("§e[-]§f")) {
							String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
							String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
							double date = Double.parseDouble(sto2) - Double.parseDouble(now());
							if (date <= 0) {
								if (p.getInventory().getContents()[i].hasItemMeta() && p.getInventory().getContents()[i].getItemMeta().getDisplayName() != null) {
									p.sendMessage(prefix + p.getInventory().getContents()[i].getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
									p.getInventory().remove(p.getInventory().getContents()[i]);
									return;
								} else if (p.getInventory().getContents()[i].hasItemMeta() && p.getInventory().getContents()[i].getItemMeta().getDisplayName() == null) {
								p.sendMessage(prefix + p.getInventory().getContents()[i].getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
								p.getInventory().remove(p.getInventory().getContents()[i]);
								return;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void scanTop(InventoryOpenEvent event) {
		Player player = (Player)event.getPlayer();
		for (int i = 0; i < event.getView().getTopInventory().getSize(); i++) {
			if (event.getView().getTopInventory().getContents()[i] != null && event.getView().getTopInventory().getContents()[i].hasItemMeta() && event.getView().getTopInventory().getContents()[i].getItemMeta().hasLore()) {
				for (int n = 0; n < event.getView().getTopInventory().getContents()[i].getItemMeta().getLore().size(); n++) {
					String a = event.getView().getTopInventory().getContents()[i].getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (event.getView().getTopInventory().getContents()[i].hasItemMeta() && event.getView().getTopInventory().getContents()[i].getItemMeta().getDisplayName() != null) {
							player.sendMessage(prefix + event.getView().getTopInventory().getContents()[i].getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.getView().getTopInventory().remove(event.getView().getTopInventory().getContents()[i]);
							return;
							}
							player.sendMessage(prefix + event.getView().getTopInventory().getContents()[i].getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.getView().getTopInventory().remove(event.getView().getTopInventory().getContents()[i]);
							return;
						}
					}
				}
			}
		}
	}
	
	public void scanBottom(InventoryOpenEvent event) {
		Player player = (Player)event.getPlayer();
		for (int i = 0; i < event.getView().getBottomInventory().getSize(); i++) {
			if (event.getView().getBottomInventory().getContents()[i] != null && event.getView().getBottomInventory().getContents()[i].hasItemMeta() && event.getView().getBottomInventory().getContents()[i].getItemMeta().hasLore()) {
				for (int n = 0; n < event.getView().getBottomInventory().getContents()[i].getItemMeta().getLore().size(); n++) {
					String a = event.getView().getBottomInventory().getContents()[i].getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (event.getView().getBottomInventory().getContents()[i].hasItemMeta() && event.getView().getBottomInventory().getContents()[i].getItemMeta().getDisplayName() != null) {
							player.sendMessage(prefix + event.getView().getBottomInventory().getContents()[i].getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.getView().getBottomInventory().remove(event.getView().getBottomInventory().getContents()[i]);
							return;
							}
							player.sendMessage(prefix + event.getView().getBottomInventory().getContents()[i].getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.getView().getBottomInventory().remove(event.getView().getBottomInventory().getContents()[i]);
							return;
						}
					}
				}
			}
		}
	}
	
	public void scanDamageIn(Entity player) {
		Player p = (Player)player;
		for (int i = 0; i < p.getInventory().getContents().length; i++) {
			if (p.getInventory().getContents()[i] != null && p.getInventory().getContents()[i].hasItemMeta() && p.getInventory().getContents()[i].getItemMeta().hasLore()) {
				for (int n = 0; n < p.getInventory().getContents()[i].getItemMeta().getLore().size(); n++) {
					String a = p.getInventory().getContents()[i].getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getInventory().getContents()[i].hasItemMeta() && p.getInventory().getContents()[i].getItemMeta().getDisplayName() != null) {
								p.sendMessage(prefix + p.getInventory().getContents()[i].getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
								p.getInventory().remove(p.getInventory().getContents()[i]);
								return;
							}
							p.sendMessage(prefix + p.getInventory().getContents()[i].getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getInventory().remove(p.getInventory().getContents()[i]);
							return;
						}
					}
				}
			}
		}
	}
	
	public void scanDamageOut(Entity player) {
		Player p = (Player)player;
		for (int i = 0; i < p.getInventory().getContents().length; i++) {
			if (p.getInventory().getContents()[i] != null && p.getInventory().getContents()[i].hasItemMeta() && p.getInventory().getContents()[i].getItemMeta().hasLore()) {
				for (int n = 0; n < p.getInventory().getContents()[i].getItemMeta().getLore().size(); n++) {
					String a = p.getInventory().getContents()[i].getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getInventory().getContents()[i].hasItemMeta() && p.getInventory().getContents()[i].getItemMeta().getDisplayName() != null) {
								p.sendMessage(prefix + p.getInventory().getContents()[i].getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
								p.getInventory().remove(p.getInventory().getContents()[i]);
								return;
							}
							p.sendMessage(prefix + p.getInventory().getContents()[i].getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getInventory().remove(p.getInventory().getContents()[i]);
							return;
						}
					}
				}
			}
		}
	}
	
	public void scanTop(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore()) {
			for (int n = 0; n < event.getCurrentItem().getItemMeta().getLore().size(); n++) {
				if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore() && event.getCurrentItem().getItemMeta().getLore().get(n).split(" ")[0].equals("§e[-]§f")) {
					scanClick(event);
					return;
				}
			}
		}
		for (int i = 0; i < event.getView().getTopInventory().getSize(); i++) {
			if (event.getView().getTopInventory().getContents()[i] != null && event.getView().getTopInventory().getContents()[i].hasItemMeta() && event.getView().getTopInventory().getContents()[i].getItemMeta().hasLore()) {
				for (int n = 0; n < event.getView().getTopInventory().getContents()[i].getItemMeta().getLore().size(); n++) {
					String a = event.getView().getTopInventory().getContents()[i].getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (event.getView().getTopInventory().getContents()[i].hasItemMeta() && event.getView().getTopInventory().getContents()[i].getItemMeta().getDisplayName() != null) {
							player.sendMessage(prefix + event.getView().getTopInventory().getContents()[i].getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.getView().getTopInventory().remove(event.getView().getTopInventory().getContents()[i]);
							return;
							}
							player.sendMessage(prefix + event.getView().getTopInventory().getContents()[i].getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.getView().getTopInventory().remove(event.getView().getTopInventory().getContents()[i]);
							return;
						}
					}
				}
			}
		}
	}
	
	public void scanBottom(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore()) {
			for (int n = 0; n < event.getCurrentItem().getItemMeta().getLore().size(); n++) {
				if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore() && event.getCurrentItem().getItemMeta().getLore().get(n).split(" ")[0].equals("§e[-]§f")) {
					scanClick(event);
					return;
				}
			}
		}
		for (int i = 0; i < event.getView().getBottomInventory().getSize(); i++) {
			if (event.getView().getBottomInventory().getContents()[i] != null && event.getView().getBottomInventory().getContents()[i].hasItemMeta() && event.getView().getBottomInventory().getContents()[i].getItemMeta().hasLore()) {
				for (int n = 0; n < event.getView().getBottomInventory().getContents()[i].getItemMeta().getLore().size(); n++) {
					String a = event.getView().getBottomInventory().getContents()[i].getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (event.getView().getBottomInventory().getContents()[i].hasItemMeta() && event.getView().getBottomInventory().getContents()[i].getItemMeta().getDisplayName() != null) {
							player.sendMessage(prefix + event.getView().getBottomInventory().getContents()[i].getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.getView().getBottomInventory().remove(event.getView().getBottomInventory().getContents()[i]);
							return;
							}
							player.sendMessage(prefix + event.getView().getBottomInventory().getContents()[i].getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.getView().getBottomInventory().remove(event.getView().getBottomInventory().getContents()[i]);
							return;
						}
					}
				}
			}
		}
	}
	
	public void scanClick(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
			if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore()) {
				for (int n = 0; n < event.getCurrentItem().getItemMeta().getLore().size(); n++) {
					String a = event.getCurrentItem().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName() != null) {
							player.sendMessage(prefix + event.getCurrentItem().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.setCurrentItem(null);
							return;
							}
							player.sendMessage(prefix + event.getCurrentItem().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							event.setCurrentItem(null);
							return;
						}
					}
				}
			}
	}
	
	public void scanEquip1(Entity player) {
		Player p = (Player)player;
			if (p.getEquipment().getHelmet() != null && p.getEquipment().getHelmet().hasItemMeta() && p.getEquipment().getHelmet().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getHelmet().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getHelmet().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getHelmet().hasItemMeta() && p.getEquipment().getHelmet().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getHelmet().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setHelmet(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getHelmet().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setHelmet(null);
							return;
						}
					}
				}
			}
	}
	
	public void scanEquip2(Entity player) {
		Player p = (Player)player;
			if (p.getEquipment().getChestplate() != null && p.getEquipment().getChestplate().hasItemMeta() && p.getEquipment().getChestplate().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getChestplate().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getChestplate().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getChestplate().hasItemMeta() && p.getEquipment().getChestplate().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getChestplate().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setChestplate(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getChestplate().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setChestplate(null);
							return;
						}
					}
				}
			}
	}
	
	public void scanEquip3(Entity player) {
		Player p = (Player)player;
			if (p.getEquipment().getLeggings() != null && p.getEquipment().getLeggings().hasItemMeta() && p.getEquipment().getLeggings().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getLeggings().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getLeggings().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getLeggings().hasItemMeta() && p.getEquipment().getLeggings().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getLeggings().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setLeggings(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getLeggings().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setLeggings(null);
							return;
						}
					}
				}
			}
	}
	
	public void scanEquip4(Entity player) {
		Player p = (Player)player;
			if (p.getEquipment().getBoots() != null && p.getEquipment().getBoots().hasItemMeta() && p.getEquipment().getBoots().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getBoots().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getBoots().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getBoots().hasItemMeta() && p.getEquipment().getBoots().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getBoots().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setBoots(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getBoots().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setBoots(null);
							return;
						}
						return;
					}
				}
			}
	}
	
	/*public void scanEquip1(InventoryOpenEvent event) {
		Player p = (Player)event.getPlayer();
			if (p.getEquipment().getHelmet().hasItemMeta() && p.getEquipment().getHelmet().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getHelmet().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getHelmet().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getHelmet().hasItemMeta() && p.getEquipment().getHelmet().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getHelmet().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setHelmet(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getHelmet().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setHelmet(null);
							return;
						}
						return;
					}
				}
			}
	}
	
	public void scanEquip2(InventoryOpenEvent event) {
		Player p = (Player)event.getPlayer();
			if (p.getEquipment().getChestplate().hasItemMeta() && p.getEquipment().getChestplate().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getChestplate().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getChestplate().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getChestplate().hasItemMeta() && p.getEquipment().getChestplate().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getChestplate().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setChestplate(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getChestplate().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setChestplate(null);
							return;
						}
						return;
					}
				}
			}
	}
	
	public void scanEquip3(InventoryOpenEvent event) {
		Player p = (Player)event.getPlayer();
			if (p.getEquipment().getLeggings().hasItemMeta() && p.getEquipment().getLeggings().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getLeggings().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getLeggings().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getLeggings().hasItemMeta() && p.getEquipment().getLeggings().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getLeggings().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setLeggings(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getLeggings().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setLeggings(null);
							return;
						}
						return;
					}
				}
			}
	}
	
	public void scanEquip4(InventoryOpenEvent event) {
		Player p = (Player)event.getPlayer();
			if (p.getEquipment().getBoots().hasItemMeta() && p.getEquipment().getBoots().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getBoots().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getBoots().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getBoots().hasItemMeta() && p.getEquipment().getBoots().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getBoots().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setBoots(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getBoots().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setBoots(null);
							return;
						}
						return;
					}
				}
			}
	}
	
	public void scanEquip1(InventoryClickEvent event) {
		Player p = (Player)event.getWhoClicked();
			if (p.getEquipment().getHelmet().hasItemMeta() && p.getEquipment().getHelmet().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getHelmet().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getHelmet().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getHelmet().hasItemMeta() && p.getEquipment().getHelmet().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getHelmet().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setHelmet(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getHelmet().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setHelmet(null);
							return;
						}
						return;
					}
				}
			}
	}
	
	public void scanEquip2(InventoryClickEvent event) {
		Player p = (Player)event.getWhoClicked();
			if (p.getEquipment().getChestplate().hasItemMeta() && p.getEquipment().getChestplate().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getChestplate().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getChestplate().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getChestplate().hasItemMeta() && p.getEquipment().getChestplate().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getChestplate().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setChestplate(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getChestplate().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setChestplate(null);
							return;
						}
						return;
					}
				}
			}
	}
	
	public void scanEquip3(InventoryClickEvent event) {
		Player p = (Player)event.getWhoClicked();
			if (p.getEquipment().getLeggings().hasItemMeta() && p.getEquipment().getLeggings().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getLeggings().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getLeggings().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getLeggings().hasItemMeta() && p.getEquipment().getLeggings().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getLeggings().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setLeggings(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getLeggings().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setLeggings(null);
							return;
						}
						return;
					}
				}
			}
	}
	
	public void scanEquip4(InventoryClickEvent event) {
		Player p = (Player)event.getWhoClicked();
			if (p.getEquipment().getBoots().hasItemMeta() && p.getEquipment().getBoots().getItemMeta().hasLore()) {
				for (int n = 0; n < p.getEquipment().getBoots().getItemMeta().getLore().size(); n++) {
					String a = p.getEquipment().getBoots().getItemMeta().getLore().get(n);
					if (a.split(" ")[0].equals("§e[-]§f")) {
						String sto = a.split(" ")[1] + a.split(" ")[2] + a.split(" ")[3] + a.split(" ")[5] + a.split(" ")[6];
						String sto2 = sto.replaceAll("년", "").replaceAll("월", "").replaceAll("일", "").replaceAll("시", "").replaceAll("분", "");
						double date = Double.parseDouble(sto2) - Double.parseDouble(now());
						if (date <= 0) {
							if (p.getEquipment().getBoots().hasItemMeta() && p.getEquipment().getBoots().getItemMeta().getDisplayName() != null) {
							p.sendMessage(prefix + p.getEquipment().getBoots().getItemMeta().getDisplayName() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setBoots(null);
							return;
							}
							p.sendMessage(prefix + p.getEquipment().getBoots().getType() + " §b의 사용 기간이 만료되어 소멸되었습니다.");
							p.getEquipment().setBoots(null);
							return;
						}
						return;
					}
				}
			}
	}*/
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onOpen(InventoryOpenEvent event) {
		scanTop(event);
		scanBottom(event);
		scanEquip1(event.getPlayer());
		scanEquip2(event.getPlayer());
		scanEquip3(event.getPlayer());
		scanEquip4(event.getPlayer());
	}
	
	/*@EventHandler(priority=EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			scanDamageOut(event.getDamager());
			scanEquip1(event.getDamager());
			scanEquip2(event.getDamager());
			scanEquip3(event.getDamager());
			scanEquip4(event.getDamager());
		}
		if (event.getEntity() instanceof Player) {
			scanDamageIn(event.getEntity());
			scanEquip1(event.getEntity());
			scanEquip2(event.getEntity());
			scanEquip3(event.getEntity());
			scanEquip4(event.getEntity());
		}
	}*/
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent event) {
		if (event.getSlotType().toString() == ("CRAFTING") && event.getCursor().hasItemMeta() && event.getCursor().getItemMeta().hasLore()) {
			for (int i = 0; i < event.getCursor().getItemMeta().getLore().size(); i++) {
				if (event.getCursor().getItemMeta().getLore().get(i).split(" ")[0].equals("§e[-]§f")) {
					((Player) event.getWhoClicked()).sendMessage("§e[WINDOWS] §c기간제 아이템은 제작칸에 넣을 수 없습니다.");
					event.setCancelled(true);
					((Player) event.getWhoClicked()).closeInventory();
					((Player) event.getWhoClicked()).updateInventory();
					return;
				}
			}
		}
		if (event.getAction().name().toString() == ("MOVE_TO_OTHER_INVENTORY") && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore()) {
			for (int i = 0; i < event.getCurrentItem().getItemMeta().getLore().size(); i++) {
				if (event.getCurrentItem().getItemMeta().getLore().get(i).split(" ")[0].equals("§e[-]§f")) {
					((Player) event.getWhoClicked()).sendMessage("§e[WINDOWS] §c기간제 아이템은 집어서만 넣을 수 있습니다.");
					event.setCancelled(true);
					((Player) event.getWhoClicked()).closeInventory();
					((Player) event.getWhoClicked()).updateInventory();
					return;
				}
			}
		}
		if (event.getSlotType().toString() == ("CRAFTING") && event.getHotbarButton() != -1) {
			((Player) event.getWhoClicked()).sendMessage("§e[WINDOWS] §c기간제 아이템은 집어서만 넣을 수 있습니다.");
			event.setResult(Result.DENY);
		}
		scanTop(event);
		scanBottom(event);
		scanEquip1(event.getWhoClicked());
		scanEquip2(event.getWhoClicked());
		scanEquip3(event.getWhoClicked());
		scanEquip4(event.getWhoClicked());
	}
}
