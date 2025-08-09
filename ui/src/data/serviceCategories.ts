import type { ServiceCategory } from '@/models/ServiceCategory'

export const serviceCategories: ServiceCategory[] = [
  { id: 1,  slug: 'shopping',        titleKey: 'SERVICE_SHOPPING_TITLE',        descKey: 'SERVICE_SHOPPING_DESC',        icon: 'shopping_basket',    image: 'https://picsum.photos/id/1080/800/600' },
  { id: 2,  slug: 'transport',       titleKey: 'SERVICE_TRANSPORT_TITLE',       descKey: 'SERVICE_TRANSPORT_DESC',       icon: 'local_shipping',     image: 'https://picsum.photos/id/1011/800/600' },
  { id: 3,  slug: 'cleaning',        titleKey: 'SERVICE_CLEANING_TITLE',        descKey: 'SERVICE_CLEANING_DESC',        icon: 'cleaning_services',  image: 'https://picsum.photos/id/454/800/600' },
  { id: 4,  slug: 'petcare',         titleKey: 'SERVICE_PETCARE_TITLE',         descKey: 'SERVICE_PETCARE_DESC',         icon: 'pets',               image: 'https://picsum.photos/id/219/800/600' },
  { id: 5,  slug: 'gardening',       titleKey: 'SERVICE_GARDENING_TITLE',       descKey: 'SERVICE_GARDENING_DESC',       icon: 'grass',              image: 'https://picsum.photos/id/94/800/600' },
  { id: 6,  slug: 'tutoring',        titleKey: 'SERVICE_TUTORING_TITLE',        descKey: 'SERVICE_TUTORING_DESC',        icon: 'school',             image: 'https://picsum.photos/id/433/800/600' },
  { id: 7,  slug: 'techhelp',        titleKey: 'SERVICE_TECHHELP_TITLE',        descKey: 'SERVICE_TECHHELP_DESC',        icon: 'computer',           image: 'https://picsum.photos/id/180/800/600' },
  { id: 8,  slug: 'childcare',       titleKey: 'SERVICE_CHILDCARE_TITLE',       descKey: 'SERVICE_CHILDCARE_DESC',       icon: 'child_care',         image: 'https://picsum.photos/id/822/800/600' },
  { id: 9,  slug: 'languagetandem',  titleKey: 'SERVICE_LANGUAGETANDEM_TITLE',  descKey: 'SERVICE_LANGUAGETANDEM_DESC',  icon: 'translate',          image: 'https://picsum.photos/id/29/800/600' },
  { id:10,  slug: 'homework',        titleKey: 'SERVICE_HOMEWORK_TITLE',        descKey: 'SERVICE_HOMEWORK_DESC',        icon: 'menu_book',          image: 'https://picsum.photos/id/24/800/600' },
  { id:11,  slug: 'repairs',         titleKey: 'SERVICE_REPAIRS_TITLE',         descKey: 'SERVICE_REPAIRS_DESC',         icon: 'build',              image: 'https://picsum.photos/id/240/800/600' },
  { id:12,  slug: 'others',          titleKey: 'SERVICE_OTHERS_TITLE',          descKey: 'SERVICE_OTHERS_DESC',          icon: 'more_horiz',         image: 'https://picsum.photos/id/10/800/600' },
]
