import { TaskCategory } from '@/models/TaskModel'

const staticTaskImages: Partial<Record<TaskCategory, string>> = {
  [TaskCategory.SHOPPING]: 'https://images.pexels.com/photos/5632402/pexels-photo-5632402.jpeg', // grocery shopping basket
  [TaskCategory.TRANSPORT]: 'https://images.pexels.com/photos/210019/pexels-photo-210019.jpeg', // road / car
  [TaskCategory.CLEANING]: 'https://images.pexels.com/photos/6195112/pexels-photo-6195112.jpeg', // cleaning supplies
  [TaskCategory.PETCARE]: 'https://images.pexels.com/photos/4587952/pexels-photo-4587952.jpeg', // walking dog
  [TaskCategory.GARDENING]: 'https://images.pexels.com/photos/450326/pexels-photo-450326.jpeg', // gardening tools
  [TaskCategory.TUTORING]: 'https://images.pexels.com/photos/5211439/pexels-photo-5211439.jpeg', // tutoring/learning
  [TaskCategory.TECHHELP]: 'https://images.pexels.com/photos/2225610/pexels-photo-2225610.jpeg', // fixing laptop
  [TaskCategory.CHILDCARE]: 'https://images.pexels.com/photos/3662796/pexels-photo-3662796.jpeg', // babysitting
  [TaskCategory.LANGUAGETANDEM]:
    'https://images.pexels.com/photos/1181686/pexels-photo-1181686.jpeg', // conversation
  [TaskCategory.HOMEWORK]: 'https://images.pexels.com/photos/4144222/pexels-photo-4144222.jpeg', // studying desk
  [TaskCategory.REPAIRS]: 'https://images.pexels.com/photos/4792495/pexels-photo-4792495.jpeg', // tools/repair
  [TaskCategory.OTHERS]: 'https://images.pexels.com/photos/5428830/pexels-photo-5428830.jpeg', // generic helper
}

const unsplashQueryByCategory: Record<TaskCategory, string> = {
  [TaskCategory.SHOPPING]: 'grocery,shopping,basket',
  [TaskCategory.TRANSPORT]: 'transport,car,delivery,road',
  [TaskCategory.CLEANING]: 'cleaning,housekeeping,cleaner',
  [TaskCategory.PETCARE]: 'pet care,dog walking,pet',
  [TaskCategory.GARDENING]: 'gardening,garden,plants',
  [TaskCategory.TUTORING]: 'tutoring,teaching,study',
  [TaskCategory.TECHHELP]: 'it support,computer repair,tech help',
  [TaskCategory.CHILDCARE]: 'childcare,babysitting,kids',
  [TaskCategory.LANGUAGETANDEM]: 'language exchange,conversation',
  [TaskCategory.HOMEWORK]: 'homework,studying,notes',
  [TaskCategory.REPAIRS]: 'repair,tools,fixing',
  [TaskCategory.OTHERS]: 'helping,community,volunteer',
}

const GLOBAL_FALLBACK = 'https://images.pexels.com/photos/5428830/pexels-photo-5428830.jpeg'

export function getTaskImage(category?: TaskCategory, existingUrl?: string): string {
  if (existingUrl) return existingUrl

  if (category && staticTaskImages[category]) {
    return staticTaskImages[category] as string
  }

  if (category) {
    const q = encodeURIComponent(unsplashQueryByCategory[category] || 'helping')
    return `https://source.unsplash.com/1600x900/?${q}`
  }

  return GLOBAL_FALLBACK
}
