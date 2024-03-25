package com.pixelmonmod.pixelmon.quests.util;

import com.pixelmonmod.pixelmon.RandomHelper;

public class NPCNames {
   private static final String[] MALE_NAMES = new String[]{"Barry", "Red", "Blue", "Green", "Ethan", "Brendan", "Lucas", "Hilbert", "Nate", "Calem", "Elio", "Chase", "Sam", "Samuel", "Jay", "Kunkka", "Percy", "Karl", "Paul", "Matt", "Matthew", "Brian", "Liam", "Caleb", "Jon", "Lorenzo", "John", "Travis", "Taliesin", "Fjord", "Sange", "Vax", "Max", "Bren", "Duncan", "Lewis", "Khan", "Jesse", "Ollie", "Oliver", "Ash", "Brock", "Clemont", "Cilan", "Gary", "Gladion", "Lance", "James", "Victor", "Wally", "Silver", "Cheren", "Hugh", "Tierno", "Trevor", "Hau", "Hop", "Leon", "Bede", "Koga", "Blaine", "Yurnero", "Brandon", "Giovanni", "Maxie", "Archie", "Cyrus", "Mars", "Saturn", "Charon", "Ghetsis", "Colress", "Faba", "Guzma", "Piers", "Rose", "Bill", "Looker", "Mollymauk", "Alec", "Khan", "Carl", "Jack", "Dan"};
   private static final String[] FEMALE_NAMES = new String[]{"Iris", "Dawn", "Bonnie", "Lusamine", "Jenny", "Joy", "Delia", "Daisy", "Diantha", "Kris", "Lyra", "Yasha", "Charlotte", "Emily", "Maria", "Veth", "Samantha", "Serena", "Rylai", "Tasha", "Laura", "Ashly", "Keyleth", "Alleria", "Mirana", "Chloe", "Vex", "Hannah", "May", "Misty", "Lillie", "Jessie", "Jester", "Hilda", "Rosa", "Selene", "Elaine", "Gloria", "Bianca", "Shauna", "Marnie", "Klara", "Erika", "Sabrina", "Jasmine", "Clair", "Fantina", "Marion", "Mallow", "Hapu", "Olivia", "Llima", "Lorelei", "Agatha", "Caitlin", "Emma", "Tabitha", "Courtney", "Shelly", "Beauregard", "Beau", "Luna", "Jupiter", "Kaya", "Wicke", "Plumeria", "Oleana", "Lanette", "Zinnia", "Bebe", "Ashley", "Marisha", "Aiushtha", "Lyralei"};

   public static String getName(int gender) {
      if (gender > 1) {
         gender = RandomHelper.rand.nextInt(2);
      }

      return gender == 0 ? MALE_NAMES[RandomHelper.rand.nextInt(MALE_NAMES.length)] : FEMALE_NAMES[RandomHelper.rand.nextInt(FEMALE_NAMES.length)];
   }
}
