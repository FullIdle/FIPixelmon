package com.pixelmonmod.pixelmon.client.models.obj.parser.obj;

import com.pixelmonmod.pixelmon.client.models.obj.Group;
import com.pixelmonmod.pixelmon.client.models.obj.WavefrontObject;
import com.pixelmonmod.pixelmon.client.models.obj.parser.LineParser;

public class GroupParser extends LineParser {
   Group newGroup = null;

   public void incoporateResults(WavefrontObject wavefrontObject) {
      if (wavefrontObject.getCurrentGroup() != null) {
         wavefrontObject.getCurrentGroup().pack();
      }

      wavefrontObject.getGroups().add(this.newGroup);
      wavefrontObject.getGroupsDirectAccess().put(this.newGroup.getName(), this.newGroup);
      wavefrontObject.setCurrentGroup(this.newGroup);
   }

   public void parse() {
      String groupName = this.words[1];
      this.newGroup = new Group(groupName);
   }
}
