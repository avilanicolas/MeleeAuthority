exports.getCharId = function(name) {
   newname = name.toLowerCase().replace(/[^a-zA-Z]/g, '');
   switch(newname) {
   case 'wireframemale':
      return 'Bo';
   case 'falcon':
   case 'captainfalcon':
      return 'Ca';
   case 'crazyhand':
      return 'Ch';
   case 'younglink':
      return 'Cl';
   case 'dk':
   case 'donkeykong':
      return 'Dk';
   case 'doctormario':
      return 'Dr';
   case 'falco':
      return 'Fc';
   case 'roy':
      return 'Fe';
   case 'fox':
      return 'Fx';
   case 'gigabowser':
      return 'Gk';
   case 'wireframefemale':
      return 'Gl';
   case 'ganondorf':
      return 'Gn';
   case 'gamewatch':
      return 'Gw';
   case 'kirby':
      return 'Kb';
   case 'bowser':
      return 'Kp';
   case 'luigi':
      return 'Lg';
   case 'link':
      return 'Lk';
   case 'masterhand':
      return 'Mh';
   case 'mario':
      return 'Mr';
   case 'marth':
      return 'Ms';
   case 'mewtwo':
      return 'Mt';
   case 'nana':
      return 'Nn';
   case 'ness':
      return 'Ns';
   case 'pichu':
      return 'Pc';
   case 'peach':
      return 'Pe';
   case 'pikachu':
      return 'Pk';
   case 'popo':
      return 'Pp';
   case 'jigglypuff':
      return 'Pr';
   case 'sandbag':
      return 'Sb';
   case 'sheik':
      return 'Sk';
   case 'samus':
      return 'Ss';
   case 'yoshi':
      return 'Ys';
   case 'zelda':
      return 'Zd';
   default:
      return name.replace(/-|\?|&|=|\\/g, '');
   }
}

exports.getName = function(name) {
   newname = name.toLowerCase().replace(/[^a-zA-Z]/g, '');
   switch(newname) {
   case 'wireframemale':
      return 'Wireframe (Male)';
   case 'falcon':
   case 'captainfalcon':
      return 'Captain Falcon';
   case 'crazyhand':
      return 'Crazy Hand';
   case 'younglink':
      return 'Young Link';
   case 'dk':
   case 'donkeykong':
      return 'Donkey Kong';
   case 'doctormario':
      return 'Doctor Mario';
   case 'falco':
      return 'Falco';
   case 'roy':
      return 'Roy';
   case 'fox':
      return 'Fox';
   case 'gigabowser':
      return 'Giga Bowser';
   case 'wireframefemale':
      return 'Wireframe (Female)';
   case 'ganondorf':
      return 'Ganondorf';
   case 'gamewatch':
      return 'Game & Watch';
   case 'kirby':
      return 'Kirby';
   case 'bowser':
      return 'Bowser';
   case 'luigi':
      return 'Luigi';
   case 'link':
      return 'Link';
   case 'masterhand':
      return 'Master Hand';
   case 'mario':
      return 'Mario';
   case 'marth':
      return 'Marth';
   case 'mewtwo':
      return 'Mewtwo';
   case 'nana':
      return 'Ice Climbers (Nana)';
   case 'ness':
      return 'Ness';
   case 'pichu':
      return 'Pichu';
   case 'peach':
      return 'Peach';
   case 'pikachu':
      return 'Pikachu';
   case 'popo':
      return 'Ice Climbers (Popo)';
   case 'jigglypuff':
      return 'Jigglypuff';
   case 'sandbag':
      return 'Sandbag';
   case 'sheik':
      return 'Sheik';
   case 'samus':
      return 'Samus';
   case 'yoshi':
      return 'Yoshi';
   case 'zelda':
      return 'Zelda';
   default:
      return name.replace(/-|\?|&|=|\\/g, '');
   }
}

exports.getFrameStrip = function (data) {
   if (!data)
      return [];
   data = data[0]['frames'];
   for (var i = 0; i < data.length; i++) {
      for (key in data[i]) {
         if (data[i][key]) {
            data[i][key] = "█";
         } else {
            data[i][key] = "";
         }
      }
   }

   return data;
}
